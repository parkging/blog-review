package com.parkging.blog.apiapp.global.config.cors;

import java.util.Arrays;
import java.util.List;

interface CorsProperties {
    List<String> ALLOWED_ORIGINS = Arrays.asList("http://localhost:3000");
    List<String> EXPOSED_HEADERS = Arrays.asList("X-Total-Count", "Authorization", "*");
    List<String> ALLOWED_HEADERS = Arrays.asList("*");
    List<String> ALLOWED_METHOD = Arrays.asList("GET", "POST", "PATCH", "DELETE");
}