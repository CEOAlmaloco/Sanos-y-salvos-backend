package com.javadiseno.sanosysalvos.analytics.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

// SY-73 Representa el evento que API Gateway envía a la Lambda
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGatewayRequestDTO {

    private String routeKey;
    private String rawPath;
    private Map<String, String> queryStringParameters;
    private RequestContext requestContext;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestContext {
        private Http http;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Http {
            private String method;
            private String path;
        }
    }

}
