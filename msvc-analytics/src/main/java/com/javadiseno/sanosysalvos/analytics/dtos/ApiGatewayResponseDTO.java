package com.javadiseno.sanosysalvos.analytics.dtos;

import lombok.*;

import java.util.Map;

// SY-73 Respuesta que API Gateway espera de la Lambda
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiGatewayResponseDTO {

    private int statusCode;
    private Map<String, String> headers;
    private String body;

    public static ApiGatewayResponseDTO ok(String jsonBody){
        return ApiGatewayResponseDTO.builder()
                .statusCode(200)
                .headers(Map.of("Content-Type", "application/json"))
                .body(jsonBody)
                .build();
    }

    public static ApiGatewayResponseDTO badRequest(String message){
        return ApiGatewayResponseDTO.builder()
                .statusCode(400)
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"message\":\"" + message + "\"}")
                .build();
    }

    public static ApiGatewayResponseDTO internalError(){
        return ApiGatewayResponseDTO.builder()
                .statusCode(500)
                .headers(Map.of("Content-Type", "application/json"))
                .body("{\"message\":\"Error interno del servidor\"}")
                .build();
    }
}
