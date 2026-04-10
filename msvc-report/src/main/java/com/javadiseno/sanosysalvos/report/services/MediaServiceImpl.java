package com.javadiseno.sanosysalvos.report.services;

import com.javadiseno.sanosysalvos.report.exceptions.ReportException;
import com.javadiseno.sanosysalvos.report.exceptions.ResourceNotFoundException;
import com.javadiseno.sanosysalvos.report.models.ReportMedia;
import com.javadiseno.sanosysalvos.report.repositories.MediaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 medios asociados a un reporte (url) alojado en s3 en el microservicio msvc-media
*/
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ReportMedia> findById(UUID id) {
        return mediaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportMedia> findByReportId(UUID reportId) {
        return mediaRepository.findByReport_Id(reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportMedia> findByReportIdOrderBySortOrderAscCreatedAtAsc(UUID reportId) {
        return mediaRepository.findByReport_IdOrderBySortOrderAscCreatedAtAsc(reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByReportId(UUID reportId) {
        return mediaRepository.countByReport_Id(reportId);
    }

    @Override
    @Transactional
    public void deleteByReportId(UUID reportId) {
        mediaRepository.deleteByReport_Id(reportId);
    }

    @Override
    @Transactional
    public ReportMedia save(ReportMedia media) {
        if (media.getReport() == null) {
            throw new ReportException("El medio debe tener un report asociado");
        }
        if (media.getUrl() == null || media.getUrl().isBlank()) {
            throw new ReportException("url es obligatoria");
        }
        return mediaRepository.save(media);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!mediaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medio no encontrado: " + id);
        }
        mediaRepository.deleteById(id);
    }
}
