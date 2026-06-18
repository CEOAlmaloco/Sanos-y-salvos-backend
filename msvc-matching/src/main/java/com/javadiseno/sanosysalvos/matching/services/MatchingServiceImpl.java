package com.javadiseno.sanosysalvos.matching.services;

import com.javadiseno.sanosysalvos.matching.dtos.BusEventDTO;
import com.javadiseno.sanosysalvos.matching.dtos.EventType;
import com.javadiseno.sanosysalvos.matching.messaging.EventBridgePublisher;
import com.javadiseno.sanosysalvos.matching.models.ReportIndexItem;
import com.javadiseno.sanosysalvos.matching.repositories.ReportIndexRepository;
import com.javadiseno.sanosysalvos.matching.util.GeoDistanceUtil;
import com.javadiseno.sanosysalvos.matching.util.GeohashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final ReportIndexRepository reportIndexRepository;
    private final RekognitionMatcher rekognitionMatcher;
    private final EventBridgePublisher eventBridgePublisher;

    @Value("${matching.score.threshold:70}")
    private double scoreThreshold;

    @Value("${matching.distance.km.max:5}")
    private double maxDistanceKm;

    @Override
    public void processEvent(BusEventDTO busEventDTO) {
        if (busEventDTO == null || busEventDTO.getDetailType() == null) {
            log.warn("Evento nulo o sin detail-type — ignorado");
            return;
        }

        try {
            EventType.valueOf(busEventDTO.getDetailType());
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de evento desconocido: {} — ignorado", busEventDTO.getDetailType());
            return;
        }

        if (EventType.match_found.name().equals(busEventDTO.getDetailType())) {
            log.info("Evento match_found recibido — no se re-procesa");
            return;
        }

        BusEventDTO.EventDetailDTO detail = busEventDTO.getDetail();
        if (detail == null || detail.getReportId() == null) {
            log.warn("Evento sin reportId — ignorado");
            return;
        }

        log.info("Procesando evento matching: type={} reportId={}", busEventDTO.getDetailType(), detail.getReportId());

        indexReport(busEventDTO);
        findMatches(busEventDTO);
    }

    private void indexReport(BusEventDTO busEventDTO) {
        BusEventDTO.EventDetailDTO detail = busEventDTO.getDetail();
        String reportId = detail.getReportId().toString();
        String petId = detail.getPetId() != null ? detail.getPetId().toString() : "unknown";
        String geohash = GeohashUtil.encode(detail.getLatitude(), detail.getLongitude());
        String indexedAt = Instant.now().toString();

        ReportIndexItem geoItem = ReportIndexItem.builder()
                .pk("GEO#" + geohash)
                .sk("REPORT#" + reportId)
                .reportId(reportId)
                .petId(petId)
                .eventType(busEventDTO.getDetailType())
                .latitude(detail.getLatitude())
                .longitude(detail.getLongitude())
                .imageObjectKey(detail.getImageObjectKey())
                .indexedAt(indexedAt)
                .build();

        ReportIndexItem petItem = ReportIndexItem.builder()
                .pk("PET#" + petId)
                .sk("REPORT#" + reportId)
                .reportId(reportId)
                .petId(petId)
                .eventType(busEventDTO.getDetailType())
                .latitude(detail.getLatitude())
                .longitude(detail.getLongitude())
                .imageObjectKey(detail.getImageObjectKey())
                .indexedAt(indexedAt)
                .build();

        reportIndexRepository.save(geoItem);
        reportIndexRepository.save(petItem);
        log.info("Reporte indexado: reportId={} geohash={}", reportId, geohash);
    }

    private void findMatches(BusEventDTO busEventDTO) {
        BusEventDTO.EventDetailDTO incoming = busEventDTO.getDetail();
        String incomingReportId = incoming.getReportId().toString();
        String geohash = GeohashUtil.encode(incoming.getLatitude(), incoming.getLongitude());

        List<ReportIndexItem> geoCandidates = reportIndexRepository.findByGeoHash(geohash);
        evaluateCandidates(incoming, incomingReportId, busEventDTO.getDetailType(), geoCandidates);

        if (incoming.getPetId() != null) {
            List<ReportIndexItem> petCandidates = reportIndexRepository.findByPetId(incoming.getPetId().toString());
            evaluateCandidates(incoming, incomingReportId, busEventDTO.getDetailType(), petCandidates);
        }
    }

    private void evaluateCandidates(
            BusEventDTO.EventDetailDTO incoming,
            String incomingReportId,
            String incomingEventType,
            List<ReportIndexItem> candidates
    ) {
        for (ReportIndexItem candidate : candidates) {
            if (incomingReportId.equals(candidate.getReportId())) {
                continue;
            }
            if (incomingEventType.equals(candidate.getEventType())) {
                continue;
            }
            if (reportIndexRepository.matchAlreadyPublished(incomingReportId, candidate.getReportId())) {
                continue;
            }

            double score = calculateScore(incoming, candidate);
            if (score < scoreThreshold) {
                continue;
            }

            publishMatch(incoming, candidate, score);
        }
    }

    private double calculateScore(BusEventDTO.EventDetailDTO incoming, ReportIndexItem candidate) {
        double score = 0;

        if (incoming.getPetId() != null && incoming.getPetId().toString().equals(candidate.getPetId())) {
            score += 50;
        }

        if (incoming.getLatitude() != null && incoming.getLongitude() != null
                && candidate.getLatitude() != null && candidate.getLongitude() != null) {
            double distanceKm = GeoDistanceUtil.distanceKm(
                    incoming.getLatitude(), incoming.getLongitude(),
                    candidate.getLatitude(), candidate.getLongitude());
            if (distanceKm <= maxDistanceKm) {
                score += Math.max(0, 40 * (1 - (distanceKm / maxDistanceKm)));
            }
        }

        Optional<Float> visualScore = rekognitionMatcher.compareFaces(
                incoming.getImageObjectKey(), candidate.getImageObjectKey());
        if (visualScore.isPresent()) {
            score = (score * 0.4) + (visualScore.get() * 0.6);
        }

        return Math.min(score, 100);
    }

    private void publishMatch(BusEventDTO.EventDetailDTO incoming, ReportIndexItem candidate, double score) {
        UUID matchedReportId = UUID.fromString(candidate.getReportId());

        BusEventDTO.EventDetailDTO matchDetail = BusEventDTO.EventDetailDTO.builder()
                .reportId(incoming.getReportId())
                .matchedReportId(matchedReportId)
                .petId(incoming.getPetId())
                .userId(incoming.getUserId())
                .latitude(incoming.getLatitude())
                .longitude(incoming.getLongitude())
                .matchScore(score)
                .eventDate(Instant.now())
                .build();

        eventBridgePublisher.publishMatchFound(matchDetail);
        reportIndexRepository.markMatchPublished(incoming.getReportId().toString(), candidate.getReportId());

        log.info("Coincidencia detectada: reportId={} matchedReportId={} score={}",
                incoming.getReportId(), matchedReportId, score);
    }
}
