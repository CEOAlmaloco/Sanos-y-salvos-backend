package com.javadiseno.sanosysalvos.integration.dtos;

import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * SY-41 Transforma el payload externo al formato interno de Report Service
 */
@Mapper(componentModel = "spring")
public interface ExternalReportMapper {

    @Mapping(source = "reportType", target = "tipo")
    @Mapping(source = "title", target = "titulo")
    @Mapping(source = "description", target = "descripcion")
    @Mapping(source = "latitude", target = "latitud")
    @Mapping(source = "longitude", target = "longitud")
    @Mapping(source = "locationText", target = "ubicacionTexto")
    @Mapping(source = "eventDate", target = "fechaReporte")
    @Mapping(source = "petId", target = "petId")
    @Mapping(target = "reporterUserId", ignore = true)
    InternalCreateReportDTO toInternal(ExternalReportRequestDTO externalReportRequestDTO);

    // Convertor de double a BigDecimal. MapStruct lo usa automáticamente en longitud y latitud
    default BigDecimal doubleToBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    // Convierte fecha local del payload externo al formato Instant que usa Report Service
    default Instant localDateTimeToInstant(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }
}
