package com.javadiseno.sanosysalvos.report.config;

import com.javadiseno.sanosysalvos.report.security.JwtAuthenticationEntryPoint;
import com.javadiseno.sanosysalvos.report.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

/**
 * SecurityConfig para el report service 
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RegexRequestMatcher feignUserReportsList =
                new RegexRequestMatcher("^/api/v1/users/[0-9a-fA-F\\-]{36}/reports/?$", HttpMethod.GET.name());
        RegexRequestMatcher feignUserReportOne =
                new RegexRequestMatcher(
                        "^/api/v1/users/[0-9a-fA-F\\-]{36}/reports/[0-9a-fA-F\\-]{36}/?$",
                        HttpMethod.GET.name());

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/h2-console/**")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/pets/**")
                                        .permitAll()
                                        .requestMatchers(feignUserReportsList)
                                        .permitAll()
                                        .requestMatchers(feignUserReportOne)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
