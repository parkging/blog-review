package com.parkging.blog.apiapp.global.config.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.setAllowedOrigins(CorsProperties.ALLOWED_ORIGINS);
        config.setExposedHeaders(CorsProperties.EXPOSED_HEADERS);
        config.setAllowedHeaders(CorsProperties.ALLOWED_HEADERS);
        config.setAllowedMethods(CorsProperties.ALLOWED_METHOD);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Value("${cors.allowed-origins}")
    public void setAllowedOrigins(List<String> allowedOrigins) {
        CorsProperties.setAllowedOrigins(allowedOrigins);;
    }
}

