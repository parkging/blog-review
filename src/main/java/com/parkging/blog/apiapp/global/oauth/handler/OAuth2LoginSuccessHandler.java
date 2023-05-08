package com.parkging.blog.apiapp.global.oauth.handler;

import com.parkging.blog.apiapp.global.auth.PrincipalDetails;
import com.parkging.blog.apiapp.global.jwt.JwtProperties;
import com.parkging.blog.apiapp.global.jwt.util.JwtSecretKeyUtil;
import com.parkging.blog.apiapp.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtSecretKeyUtil jwtSecretKeyUtil;
    @Value("${client.url}")
    private String CLIENT_URL;
    @Value("${client.port}")
    private String CLIENT_PORT;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2LoginSuccessHandler.onAuthenticationSuccess");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String jwt = JwtUtil.getJwtToken(principalDetails, jwtSecretKeyUtil.getJwtSecret(), JwtProperties.JWT_EXPIRATION_MINUTE);
        String refreshToken = JwtUtil.getJwtToken(principalDetails, jwtSecretKeyUtil.getRefSecret(), JwtProperties.REF_EXPIRATION_MINUTE);
        String refreshTokenCookie = JwtUtil.getRefreshTokenCookie(refreshToken);
        String refreshTokenExpireTimeCookie = JwtUtil.getRefreshTokenExpireTimeCookie();
        String redirectURL = CLIENT_URL;
        if(!CLIENT_PORT.isBlank()) {
            redirectURL += ":" + CLIENT_PORT;
        }

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwt);
        response.setHeader("Set-Cookie", refreshTokenCookie);
        response.addHeader("Set-Cookie", refreshTokenExpireTimeCookie);
        response.sendRedirect(redirectURL);
    }
}
