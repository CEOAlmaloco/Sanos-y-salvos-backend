package com.javadiseno.sanosysalvos.integration.service;

import com.javadiseno.sanosysalvos.integration.client.ReportServiceClient;
import com.javadiseno.sanosysalvos.integration.client.ReportServiceFeignInterceptor;
import com.javadiseno.sanosysalvos.integration.dto.*;
import com.javadiseno.sanosysalvos.integration.exception.ReportServiceException;
import com.javadiseno.sanosysalvos.integration.model.InstitutionApiKey;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationServiceImpl implements IntegrationService{

    private final ExternalReportMapper externalReportMapper;
    private final ReportServiceClient reportServiceClient;

    @Override
    public ExternalReportResponseDTO processExternalReport(
            InstitutionApiKey institution,
            ExternalReportRequestDTO externalReportRequestDTO
    ){
        InternalCreateReportDTO internalDto = externalReportMapper.toInternal(externalReportRequestDTO);
        internalDto.setReporterUserId(institution.getUserId());

        ReportSummaryResponse reportResponse;
        try{

            ReportServiceFeignInterceptor.setCurrentUserId(institution.getUserId());
            reportResponse = reportServiceClient.createReport(internalDto);

        } catch (FeignException exception){

            log.error("Error llamando a Report service: status={} body={}", exception.status(), exception.contentUTF8());
            throw new ReportServiceException(exception.status(), exception.contentUTF8());

        } finally {
            ReportServiceFeignInterceptor.clearCurrentUserId();
        }

        log.info("Reporte creado\nInstitución: {} externalId: {} internalId: {}",
                institution.getInstitutionName(), externalReportRequestDTO.getExternalReportId(), reportResponse.getId());

        return ExternalReportResponseDTO.builder()
                .externalReportId(externalReportRequestDTO.getExternalReportId())
                .internalReportId(reportResponse.getId())
                .message("Reporte Creado exitosamente")
                .build();
    }
}
