package com.parkging.blog.apiapp.global.jwt.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretKeyUtil {
    @Value("${jwt.token.jwt_secret}")
    private String JWT_SECRET;
    @Value("${jwt.token.ref_secret}")
    private String REF_SECRET;

    public String getJwtSecret() {
        return this.JWT_SECRET;
    }

    public String getRefSecret() {
        return this.REF_SECRET;
    }
}
