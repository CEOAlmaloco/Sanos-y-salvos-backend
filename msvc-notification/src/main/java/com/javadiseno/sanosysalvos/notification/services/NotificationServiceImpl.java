package com.javadiseno.sanosysalvos.notification.services;

import com.javadiseno.sanosysalvos.notification.dtos.BusEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SnsClient snsClient;

    @Value("${notification.sns.topic-arn:}")
    private String topicArn;

    @Override
    public void processEvent(BusEventDTO busEventDTO) {
        if (busEventDTO == null || busEventDTO.getDetailType() == null) {
            log.warn("Evento nulo o sin detail-type — ignorado");
            return;
        }

        String detailType = busEventDTO.getDetailType();
        BusEventDTO.EventDetailDTO detail = busEventDTO.getDetail();

        log.info("Procesando notificación: type={} reportId={}",
                detailType,
                detail != null && detail.getReportId() != null ? detail.getReportId() : "n/a");

        String message = buildMessage(detailType, detail);
        String subject = buildSubject(detailType);

        publish(subject, message);
    }

    private String buildSubject(String detailType) {
        return switch (detailType) {
            case "pet_reported" -> "Sanos y Salvos — Nuevo reporte de mascota";
            case "pet_found" -> "Sanos y Salvos — Mascota encontrada";
            case "match_found" -> "Sanos y Salvos — ¡Coincidencia detectada!";
            default -> "Sanos y Salvos — Evento " + detailType;
        };
    }

    private String buildMessage(String detailType, BusEventDTO.EventDetailDTO detail) {
        if (detail == null) {
            return "Evento " + detailType + " sin detalle.";
        }

        return switch (detailType) {
            case "pet_reported" -> String.format(
                    "Se registró un nuevo reporte.%nreportId: %s%npetId: %s%nubicación: %s, %s",
                    detail.getReportId(),
                    detail.getPetId(),
                    detail.getLatitude(),
                    detail.getLongitude());
            case "pet_found" -> String.format(
                    "Un reporte fue marcado como encontrado.%nreportId: %s%npetId: %s",
                    detail.getReportId(),
                    detail.getPetId());
            case "match_found" -> String.format(
                    "Posible coincidencia entre reportes.%nreportId: %s%nmatchedReportId: %s%nscore: %s",
                    detail.getReportId(),
                    detail.getMatchedReportId(),
                    detail.getMatchScore());
            default -> "Evento " + detailType + " — reportId=" + detail.getReportId();
        };
    }

    private void publish(String subject, String message) {
        if (topicArn == null || topicArn.isBlank()) {
            log.warn("SNS_TOPIC_ARN no configurado — notificación solo en log: subject={}", subject);
            log.info("Mensaje: {}", message);
            return;
        }

        try {
            snsClient.publish(PublishRequest.builder()
                    .topicArn(topicArn)
                    .subject(subject)
                    .message(message)
                    .build());
            log.info("Notificación SNS enviada: subject={}", subject);
        } catch (Exception e) {
            log.error("Error enviando SNS: {}", e.getMessage());
        }
    }
}
