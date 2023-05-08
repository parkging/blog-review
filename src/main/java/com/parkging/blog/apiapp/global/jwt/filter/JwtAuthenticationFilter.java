package com.parkging.blog.apiapp.global.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.global.auth.PrincipalDetails;
import com.parkging.blog.apiapp.global.jwt.JwtProperties;
import com.parkging.blog.apiapp.global.jwt.util.JwtSecretKeyUtil;
import com.parkging.blog.apiapp.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * POST로 /login url 요청 시 동작
 * /login 요청 시 eamil, password 전송 (POST) 시 UsernamePasswordAuthenticationFilter 가 기본동작.
 * email, pawssword 검증 후 response에 JWT를, cookie에 refreshToken을 담아서 응답.
 * 검증 실패 시 throw AuthenticationException 후 ExceptionHandlerFilter 에서 오류 응답.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //superClass에서 사용됨; 필수선언
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtSecretKeyUtil jwtSecretKeyUtil;

    /**
     * Post로 "/login" 요청 시 실행
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter.attemptAuthentication");
        /**
         * 1. 수신 된 email, password 로그인 시도 => DI 받은 AuthenticationManager 로 로그인 시도 시 PrincipalDetailsService 호출
         * 3. PrincipalDetailsService 에서 attemptAuthentication 메쏘드 호출
         * 4. PrincipalDetails를 세션에 담고 JWT 토큰 생성 후 응답
         */
        try {
            Member member = objectMapper.readValue(request.getInputStream(), Member.class);

            /**
             * PrincipalDetailsService.loadUserByUsername() 실행
             * 인증 실패 => throw AuthenticationException(InternalAuthenticationServiceException | BadCredentialsException ...)
             * 인증 성공 => Authentication return
             */
            Authentication authentication = JwtUtil.getAuthentication(member, authenticationManager);

            //return 시 authentication 객체가 session 영역에 저장됨. session은 인가에만 일시적으로 사용
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return null;
    }

    /**
     * attemptAuthentication 실행 후 정상 인증인 경우 실행; JWT 토큰 생성 후 response
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter.successfulAuthentication : 인증 정상");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwt = JwtUtil.getJwtToken(principalDetails, jwtSecretKeyUtil.getJwtSecret(), JwtProperties.JWT_EXPIRATION_MINUTE);
        String refreshToken = JwtUtil.getJwtToken(principalDetails, jwtSecretKeyUtil.getRefSecret(), JwtProperties.REF_EXPIRATION_MINUTE);
        String refreshTokenCookie = JwtUtil.getRefreshTokenCookie(refreshToken);
        String refreshTokenExpireTimeCookie = JwtUtil.getRefreshTokenExpireTimeCookie();

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwt);
        response.setHeader("Set-Cookie", refreshTokenCookie);
        response.addHeader("Set-Cookie", refreshTokenExpireTimeCookie);
    }

    /**
     * attemptAuthentication 실행 후 인증 실패인 경우 실행
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter.unsuccessfulAuthentication : 인증 실패");
        log.info("", failed);
        throw failed;
    }
}
