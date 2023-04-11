package com.parkging.blog.apiapp.global.filter;

import com.parkging.blog.apiapp.global.exception.ErrorMessageUtil;
import com.parkging.blog.apiapp.global.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ErrorMessageUtil eu;
    private static final String LOGIN_ERROR_CODE = "error.login.fail";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e){
            //인증  실패
            ErrorResult errorResult = eu.getErrorResult(LOGIN_ERROR_CODE, null, null);
            eu.setErrorResponse(response, errorResult);
        }

    }
}
