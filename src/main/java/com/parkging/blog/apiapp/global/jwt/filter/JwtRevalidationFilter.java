package com.parkging.blog.apiapp.global.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.parkging.blog.apiapp.domain.member.domain.Member;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import com.parkging.blog.apiapp.global.auth.PrincipalDetails;
import com.parkging.blog.apiapp.global.exception.RevalidationException;
import com.parkging.blog.apiapp.global.jwt.JwtProperties;
import com.parkging.blog.apiapp.global.jwt.util.JwtSecretKeyUtil;
import com.parkging.blog.apiapp.global.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * POST로 /silent-refresh url 요청 시 동작
 * refreshToken 검증 후 JWT 재발급하여 응답
 * 갱신 실패 시 trhow RevalidationException 후 ExceptionHandlerFilter 에서 오류응답 처리.
 */
@Slf4j
@RequiredArgsConstructor
public class JwtRevalidationFilter extends UsernamePasswordAuthenticationFilter {
    //superClass에서 사용됨; 필수선언
    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final JwtSecretKeyUtil jwtSecretKeyUtil;

    /**
     * Post로 "/silent-refresh" 요청 시 실행
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     * redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication
     * @throws AuthenticationException | RevalidationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtRevalidationFilter.attemptAuthentication");

        String refreshToken = JwtUtil.getRefreshTokenByCookies(request.getCookies());

        //refreshToken 미존재 -> 재로그인 고지
        if(refreshToken == null) {
            throw new RevalidationException();
        }

        try {
            //refreshToken 검증
            String userEmail = JwtUtil.verifyToken(refreshToken, jwtSecretKeyUtil.getRefSecret());

            // 토큰 서명이 정상적으로 이루어짐
            if (userEmail != null) {
                Member findMember = memberService.findByEmail(userEmail);
                Authentication authentication = JwtUtil.getAuthentication(findMember);
                return authentication;
            }

        } catch (JWTVerificationException ex) {
            log.info("JWT 갱신 실패");
            throw new RevalidationException(ex);
        }

        return null;
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
        log.info("JwtRevalidationFilter.successfulAuthentication : 인증 정상");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // 시큐리티 세션에 강제로 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authResult);

        String jwtToken = JwtUtil.getJwtToken(principalDetails, jwtSecretKeyUtil.getJwtSecret(), JwtProperties.JWT_EXPIRATION_MINUTE);

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

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
        log.info("JwtRevalidationFilter.unsuccessfulAuthentication : 갱신 실패");
        throw failed;
    }
}
