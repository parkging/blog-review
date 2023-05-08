package com.parkging.blog.apiapp.global.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import com.parkging.blog.apiapp.global.exception.ErrorMessageUtil;
import com.parkging.blog.apiapp.global.exception.ErrorResult;
import com.parkging.blog.apiapp.global.exception.RevalidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ErrorMessageUtil eu;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e){
            //인증 실패
            log.info("인증 실패",e);;

            ErrorResult errorResult = eu.getErrorResult("error.login.fail", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.UNAUTHORIZED);
        } catch (RevalidationException e) {
            //갱신 실패
            log.info("갱신 실패",e);;

            ErrorResult errorResult = eu.getErrorResult("error.login.revalidation", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.UNAUTHORIZED);
        } catch (TokenExpiredException e) {
            //JWT 만료
            log.info("JWT 만료",e);

            ErrorResult errorResult = eu.getErrorResult("error.login.expired", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.UNAUTHORIZED);
        } catch (JWTVerificationException e) {
            //JWT토큰 검증 실패
            log.info("JWT인증 실패",e);;

            ErrorResult errorResult = eu.getErrorResult("error.login.fail", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.UNAUTHORIZED);
        } catch (org.springframework.security.access.AccessDeniedException e) {
            log.info("권한 부족",e);;

            ErrorResult errorResult = eu.getErrorResult("error.forbidden", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.FORBIDDEN);
        } catch (IllegalArgumentException e) {
            log.info("",e);;

            ErrorResult errorResult = eu.getErrorResult("error.badrequest", null, null);
            eu.setErrorResponse(response, errorResult, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("", e);
            ErrorResult errorResult = eu.getErrorResult(e);
            eu.setErrorResponse(response, errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
