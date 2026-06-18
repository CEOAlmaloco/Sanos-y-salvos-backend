package com.javadiseno.sanosysalvos.analytics.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javadiseno.sanosysalvos.analytics.dtos.ApiGatewayRequestDTO;
import com.javadiseno.sanosysalvos.analytics.dtos.HotZoneDTO;
import com.javadiseno.sanosysalvos.analytics.dtos.StatsResponseDTO;
import com.javadiseno.sanosysalvos.analytics.services.HotZoneService;
import com.javadiseno.sanosysalvos.analytics.services.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

// SY-73 Handler Lambda de lectura, expuesto via API Gateway
@Slf4j
@Component
@RequiredArgsConstructor
/**
 * Devuelve JSON plano (String). Spring Cloud Function + API Gateway HTTP v2
 * envuelven el resultado en {statusCode, body}; si devolvemos ApiGatewayResponseDTO
 * se produce doble encapsulado y el cliente ve el wrapper completo.
 */
public class AnalyticsQueryHandler implements Function<ApiGatewayRequestDTO, String> {

    private final StatsService statsService;
    private final HotZoneService hotZoneService;
    private final ObjectMapper objectMapper;

    @Override
    public String apply(ApiGatewayRequestDTO apiGatewayRequestDTO){
        try{
            String path = resolvePath(apiGatewayRequestDTO);
            log.info("AnalyticsQueryHandler - path: {}", path);

            return switch (path) {
                case "/analytics/stats" -> handleStats(apiGatewayRequestDTO);
                case "/analytics/hot-zones" -> handleHotZones();
                default -> objectMapper.writeValueAsString(Map.of("message", "Ruta no encontrada: " + path));
            };
        } catch (Exception e){
            log.error("Error en AnalyticsQueryHandler: {}", e.getMessage());
            try {
                return objectMapper.writeValueAsString(Map.of("message", "Error interno del servidor"));
            } catch (Exception ex) {
                return "{\"message\":\"Error interno del servidor\"}";
            }
        }
    }

    // Handlers por ruta

    private String handleStats(ApiGatewayRequestDTO apiGatewayRequestDTO) throws Exception {
        Map<String, String> params = apiGatewayRequestDTO.getQueryStringParameters();

        StatsResponseDTO stats;
        if (params != null && params.containsKey("from") && params.containsKey("to")) {

            // GET /analytics/stats?from=2026-05-01&to=2026-05-31
            stats = statsService.getStatsByDateRange(params.get("from"), params.get("to"));

        } else {

            // GET /analytics/stats
            stats = statsService.getGeneralStats();
        }

        return objectMapper.writeValueAsString(stats);
    }

    private String handleHotZones() throws Exception {
        List<HotZoneDTO> hotZones = hotZoneService.getHotZones();
        return objectMapper.writeValueAsString(hotZones);
    }

    // Helpers

    private String resolvePath(ApiGatewayRequestDTO apiGatewayRequestDTO){
        if (apiGatewayRequestDTO.getRawPath() != null) return apiGatewayRequestDTO.getRawPath();
        if (apiGatewayRequestDTO.getRequestContext() != null && apiGatewayRequestDTO.getRequestContext().getHttp() != null) {
            return apiGatewayRequestDTO.getRequestContext().getHttp().getPath();
        }
        return "";
    }
}

