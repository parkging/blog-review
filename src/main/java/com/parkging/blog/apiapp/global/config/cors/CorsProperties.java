package com.parkging.blog.apiapp.global.config.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
class CorsProperties {
    static List<String> ALLOWED_ORIGINS;

    public static void setAllowedOrigins(List<String> allowedOrigins) {
        ALLOWED_ORIGINS = allowedOrigins;
    }

    static final List<String> EXPOSED_HEADERS = Arrays.asList("X-Total-Count", "Authorization", "*");
    static final List<String> ALLOWED_HEADERS = Arrays.asList("*");
    static final List<String> ALLOWED_METHOD = Arrays.asList("GET", "POST", "PATCH", "DELETE");
}