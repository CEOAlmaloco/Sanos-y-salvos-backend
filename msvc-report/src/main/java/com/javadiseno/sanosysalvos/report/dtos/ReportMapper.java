package com.javadiseno.sanosysalvos.report.dtos;

import com.javadiseno.sanosysalvos.report.dtos.requests.CreateMediaLinkRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.CreateReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.CreateSightingRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.models.ReportMedia;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.models.ReportModel.ReportType;
import com.javadiseno.sanosysalvos.report.models.ReportSighting;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public final class ReportMapper {

    private ReportMapper() {}

    public static ReportResponse toResponse(ReportModel entity) {
        if (entity == null) {
            return null;
        }
        ReportResponse r = new ReportResponse();
        r.setId(entity.getId());
        r.setPetId(entity.getPetId());
        r.setReporterUserId(entity.getReporterUserId());
        r.setType(entity.getType());
        r.setTitle(entity.getTitle());
        r.setDescription(entity.getDescription());
        r.setLatitude(entity.getLatitude());
        r.setLongitude(entity.getLongitude());
        r.setLocationDescription(entity.getLocationDescription());
        r.setReportedAt(entity.getReportedAt());
        r.setStatus(entity.getStatus());
        r.setResolvedAt(entity.getResolvedAt());
        r.setCreatedAt(entity.getCreatedAt());
        return r;
    }

    public static List<ReportResponse> toResponseList(List<ReportModel> list) {
        return list.stream().map(ReportMapper::toResponse).toList();
    }

    public static SightingResponse toSightingResponse(ReportSighting entity) {
        if (entity == null) {
            return null;
        }
        SightingResponse r = new SightingResponse();
        r.setId(entity.getId());
        r.setReportId(entity.getReport() != null ? entity.getReport().getId() : null);
        r.setSpottedAt(entity.getSpottedAt());
        r.setNotes(entity.getNotes());
        r.setCreatedAt(entity.getCreatedAt());
        return r;
    }

    public static List<SightingResponse> toSightingResponseList(List<ReportSighting> list) {
        return list.stream().map(ReportMapper::toSightingResponse).toList();
    }

    public static MediaLinkResponse toMediaLinkResponse(ReportMedia entity) {
        if (entity == null) {
            return null;
        }
        MediaLinkResponse r = new MediaLinkResponse();
        r.setId(entity.getId());
        r.setReportId(entity.getReport() != null ? entity.getReport().getId() : null);
        r.setUrl(entity.getUrl());
        r.setSortOrder(entity.getSortOrder());
        r.setCreatedAt(entity.getCreatedAt());
        return r;
    }

    public static List<MediaLinkResponse> toMediaLinkResponseList(List<ReportMedia> list) {
        return list.stream().map(ReportMapper::toMediaLinkResponse).toList();
    }

    public static ReportModel toNewEntity(CreateReportRequest req, UUID reporterUserIdEfectivo) {
        if (req.getPet() != null && req.getPetId() == null) {
            throw new ReportException(
                    "Alta de mascota embebida (objeto pet) pendiente de orquestación con Pet Service; envíe petId.");
        }
        ReportType type = parseType(req.getType());
        ReportModel m = new ReportModel();
        m.setPetId(req.getPetId());
        m.setReporterUserId(reporterUserIdEfectivo);
        m.setType(type);
        m.setTitle(req.getTitle());
        m.setDescription(req.getDescription());
        m.setLatitude(req.getLatitude());
        m.setLongitude(req.getLongitude());
        m.setLocationDescription(req.getLocationText());
        m.setReportedAt(req.getReportedAt() != null ? req.getReportedAt() : java.time.Instant.now());
        return m;
    }

    public static void applyPatch(ReportModel target, PatchReportRequest p) {
        if (p.getTitle() != null) {
            target.setTitle(p.getTitle());
        }
        if (p.getDescription() != null) {
            target.setDescription(p.getDescription());
        }
        if (p.getLatitude() != null) {
            target.setLatitude(p.getLatitude());
        }
        if (p.getLongitude() != null) {
            target.setLongitude(p.getLongitude());
        }
        if (p.getLocationText() != null) {
            target.setLocationDescription(p.getLocationText());
        }
        if (p.getReportedAt() != null) {
            target.setReportedAt(p.getReportedAt());
        }
    }

    public static ReportSighting toNewSighting(CreateSightingRequest req, ReportModel report) {
        ReportSighting s = new ReportSighting();
        s.setReport(report);
        s.setSpottedAt(req.getSpottedAt());
        s.setNotes(req.getNotes());
        return s;
    }

    public static ReportMedia toNewMediaLink(CreateMediaLinkRequest req, ReportModel report) {
        ReportMedia m = new ReportMedia();
        m.setReport(report);
        m.setUrl(req.getUrl());
        m.setSortOrder(req.getSortOrder());
        return m;
    }

    static ReportType parseType(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ReportException("El tipo es obligatorio");
        }
        String t = raw.trim().toUpperCase(Locale.ROOT);
        return switch (t) {
            case "PERDIDO", "LOST" -> ReportType.LOST;
            case "ENCONTRADO", "FOUND" -> ReportType.FOUND;
            case "AVISTAMIENTO", "SIGHTING" -> ReportType.SIGHTING;
            default -> throw new ReportException("Tipo inválido: " + raw);
        };
    }
}
