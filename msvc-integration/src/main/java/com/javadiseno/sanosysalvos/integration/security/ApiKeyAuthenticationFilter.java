package com.javadiseno.sanosysalvos.integration.security;

import com.javadiseno.sanosysalvos.integration.model.InstitutionApiKey;
import com.javadiseno.sanosysalvos.integration.repository.InstitutionApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * SY-40 Filtro que valida la API key en cada request a api/v1/integration
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    public static final String INSTITUTION_ATTRIBUTE = "authenticatedInstitution";
    public static final String API_KEY_HEADER = "X-Api-Key";
    private final InstitutionApiKeyRepository institutionApiKeyRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().startsWith("/api/v1/integration")){
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || apiKey.isBlank()) {
            log.warn("Request a {} sin header {}", request.getRequestURI(), API_KEY_HEADER);
            writeUnauthorized(response, "Header X-Api-Key es requerido");
            return;
        }

        InstitutionApiKey institutionApiKey = institutionApiKeyRepository
                .findByApiKey(apiKey)
                .orElse(null);

        if (institutionApiKey == null){
            log.warn("API key inválida: {}...", apiKey.substring(0, Math.min(6, apiKey.length())));
            writeUnauthorized(response, "API key inválida");
            return;
        }

        log.info("Institución autenticada: {}", institutionApiKey.getInstitutionName());
        request.setAttribute(INSTITUTION_ATTRIBUTE, institutionApiKey);

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(
                "{\"status\":401,\"message\":\"" + message + "\"}"
        );
    }

}
