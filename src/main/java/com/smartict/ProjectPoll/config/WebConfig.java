package com.smartict.ProjectPoll.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SurveyAllowedHosts allowedHosts;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(this.allowedHosts.getAllowedHosts());
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count"));
        config.setAllowCredentials(true);
        config.setMaxAge(1800L);
        List<String> allowedOrigins = config.getAllowedOrigins();
        if (!allowedOrigins.isEmpty()) {
            source.registerCorsConfiguration("/**", config);
        }
        return new CorsFilter(source);
    }
}