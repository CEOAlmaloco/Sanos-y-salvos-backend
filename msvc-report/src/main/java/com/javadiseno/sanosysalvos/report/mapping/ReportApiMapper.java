package com.javadiseno.sanosysalvos.report.mapping;

import com.javadiseno.sanosysalvos.report.dtos.requests.CreateReportRequest;
import com.javadiseno.sanosysalvos.report.dtos.requests.PatchReportRequest;
import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.models.ReportModel;
import com.javadiseno.sanosysalvos.report.models.ReportModel.ReportType;
import java.util.Locale;
import java.util.UUID;

public final class ReportApiMapper {

    private ReportApiMapper() {}

    public static ReportModel toNewEntity(CreateReportRequest req, UUID reporterUserIdEfectivo) {
        if (req.getPet() != null && req.getPetId() == null) {
            throw new ReportException(
                    "Alta de mascota embebida (objeto pet) pendiente de orquestación con Pet Service; envíe petId.");
        }
        if (req.getPetId() == null) {
            throw new ReportException("petId es obligatorio hasta integrar creación automática de mascota.");
        }
        if (reporterUserIdEfectivo == null) {
            throw new ReportException("reporterUserId es obligatorio (body o cabecera X-User-Id).");
        }
        ReportType type = parseTipo(req.getTipo());
        ReportModel m = new ReportModel();
        m.setPetId(req.getPetId());
        m.setReporterUserId(reporterUserIdEfectivo);
        m.setType(type);
        m.setTitle(req.getTitulo());
        m.setDescription(req.getDescripcion());
        m.setLatitude(req.getLatitud());
        m.setLongitude(req.getLongitud());
        m.setLocationDescription(req.getUbicacionTexto());
        m.setReportedAt(req.getFechaReporte() != null ? req.getFechaReporte() : java.time.Instant.now());
        return m;
    }

    public static void applyPatch(ReportModel target, PatchReportRequest p) {
        if (p.getTitulo() != null) {
            target.setTitle(p.getTitulo());
        }
        if (p.getDescripcion() != null) {
            target.setDescription(p.getDescripcion());
        }
        if (p.getLatitud() != null) {
            target.setLatitude(p.getLatitud());
        }
        if (p.getLongitud() != null) {
            target.setLongitude(p.getLongitud());
        }
        if (p.getUbicacionTexto() != null) {
            target.setLocationDescription(p.getUbicacionTexto());
        }
        if (p.getFechaReporte() != null) {
            target.setReportedAt(p.getFechaReporte());
        }
    }

    static ReportType parseTipo(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ReportException("tipo es obligatorio");
        }
        String t = raw.trim().toUpperCase(Locale.ROOT);
        return switch (t) {
            case "PERDIDO", "LOST" -> ReportType.LOST;
            case "ENCONTRADO", "FOUND" -> ReportType.FOUND;
            case "AVISTAMIENTO", "SIGHTING" -> ReportType.SIGHTING;
            default -> throw new ReportException("tipo inválido: " + raw);
        };
    }
}
