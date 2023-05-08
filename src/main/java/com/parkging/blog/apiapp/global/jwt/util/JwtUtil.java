package com.parkging.blog.apiapp.global.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.global.auth.PrincipalDetails;
import com.parkging.blog.apiapp.global.exception.RevalidationException;
import com.parkging.blog.apiapp.global.jwt.JwtProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

public final class JwtUtil {

    public static String getJwtToken(PrincipalDetails principalDetails, String jwtSecretKey, Long jwtExpirationMinute) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(jwtExpirationMinute)))
                .withClaim("id", principalDetails.getId())
                .withClaim("email", principalDetails.getUsername())
                .withClaim("expire_millisecond", jwtExpirationMinute * 60 * 1000)
                .sign(Algorithm.HMAC512(jwtSecretKey));
    }

    public static String getRefreshTokenByCookies(Cookie[] cookies) {
        if(cookies == null) throw new RevalidationException();
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(JwtProperties.REFRESH_TOKEN_NAME))
                .findFirst()
                .orElseThrow(() -> new RevalidationException())
                .getValue();
    }

    public static String getRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(JwtProperties.REFRESH_TOKEN_NAME, refreshToken)
                .maxAge(JwtProperties.REF_EXPIRATION_MINUTE * 60)
                .domain(JwtProperties.JWT_DOMAIN)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build()
                .toString();
    }

    //리프레시토큰 만료시간 쿠키 생성(httpOnly 아님); 해당 쿠키로 클라이언트에서 리프레시토큰 여부 확인
    public static String getRefreshTokenExpireTimeCookie() {
        return ResponseCookie.from(JwtProperties.REFRESH_EXPIRE_TIME, Long.toString(JwtProperties.REF_EXPIRATION_MINUTE * 60))
                .maxAge(JwtProperties.REF_EXPIRATION_MINUTE * 60)
                .path("/")
                .domain(JwtProperties.JWT_DOMAIN)
                .secure(true)
                .sameSite("None")
                .httpOnly(false)
                .build()
                .toString();
    }

    /**
     *
     * @param jwtToken
     * @param jwtSecret
     * @return String userEmail
     * @Throws JWTVerificationException (
                SignatureVerificationException – Sign 실패 시
                TokenExpiredException – 토큰 만료 시
                InvalidClaimException – claim 미존재 시
              )
     */
    public static String verifyToken(String jwtToken, String jwtSecret) {
        return JWT.require(Algorithm.HMAC512(jwtSecret)).build()
                .verify(jwtToken)
                .getClaim("email")
                .asString();
    }

    //Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 반환
    public static Authentication getAuthentication(Member member) {

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    //사용자 정보 검증 후 Authentication 반환
    public static Authentication getAuthentication(Member member, AuthenticationManager authenticationManager) {

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword()));
    }
}
