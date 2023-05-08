package com.parkging.blog.apiapp.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ErrorMessageUtil {
    private final MessageSource ms;
    private static final String EXCEPTION_ERROR_CODE = "error";

    public String getErrorCode(Exception e) {
        String errorCode = e.getMessage();
        try {
            ms.getMessage(errorCode, null, Locale.KOREA);
        } catch (Exception ex) {
            return EXCEPTION_ERROR_CODE;
        }
        return errorCode;
    }
    public String getErrorMessage(Exception e) {
        String errorCode = e.getMessage();
        try {
            return ms.getMessage(errorCode, null, Locale.KOREA);
        } catch (Exception ex) {
            return ms.getMessage(EXCEPTION_ERROR_CODE, null, Locale.KOREA);
        }
    }
    public String getErrorMessage(Exception e, String[] args) {
        String errorCode = e.getMessage();
        try {
            return ms.getMessage(errorCode, args, Locale.KOREA);
        } catch (Exception ex) {
            return ms.getMessage(EXCEPTION_ERROR_CODE, null, Locale.KOREA);
        }
    }
    public String getErrorMessage(String errorCode, String[] args) {
        try {
            return ms.getMessage(errorCode, args, Locale.KOREA);
        } catch (Exception ex) {
            return ms.getMessage(EXCEPTION_ERROR_CODE, null, Locale.KOREA);
        }
    }

    public ErrorResult getErrorResult(Exception e) {
        return ErrorResult.builder()
                .errorCode(getErrorCode(e))
                .message(getErrorMessage(e))
                .build();
    }

    public ErrorResult getErrorResult(Exception e, String redirectUrl) {
        return ErrorResult.builder()
                .errorCode(getErrorCode(e))
                .message(getErrorMessage(e))
                .redirectUrl(redirectUrl)
                .build();
    }

    public ErrorResult getErrorResult(String errorCode, String[] args, String redirectUrl) {
        return ErrorResult.builder()
                .errorCode(errorCode)
                .message(getErrorMessage(errorCode, args))
                .build();
    }

    public void setErrorResponse(HttpServletResponse response, ErrorResult errorResult){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResult));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setErrorResponse(HttpServletResponse response, ErrorResult errorResult, HttpStatus httpStatus){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResult));
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
