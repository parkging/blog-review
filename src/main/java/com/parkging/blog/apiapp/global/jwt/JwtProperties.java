package com.parkging.blog.apiapp.global.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class JwtProperties {
    public final static Long JWT_EXPIRATION_MINUTE = 10L;
    public final static Long REF_EXPIRATION_MINUTE = 180L;
    public final static String TOKEN_PREFIX = "Bearer ";
    public final static String HEADER_STRING = "Authorization";
    public final static String REFRESH_TOKEN_NAME = "refreshToken";
    public final static String REFRESH_EXPIRE_TIME = "refreshExpireTime";
    public final static String REFRESH_TOKEN_URL = "/silent-refresh";
    public final static String JWT_USERNAME_PARAMETER = "email";

    public static String JWT_DOMAIN;
    @Value("${jwt.domain}")
    public void setJWT_DOMAIN(String value) {
        JWT_DOMAIN = value;
    }
}
