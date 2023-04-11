package com.parkging.blog.apiapp.global.config.jwt;

public interface JwtProperties {
    String SECRET = "secret.blog.parkging.com";
    Long EXPIRATION_MINUTE = 10L;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
