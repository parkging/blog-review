package com.parkging.blog.apiapp.global.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        MDC.put("transactionId", UUID.randomUUID().toString() + " ");

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        printRequestLog((HttpServletRequest) request, requestWrapper, responseWrapper);

        chain.doFilter(requestWrapper, responseWrapper);

        long end = System.currentTimeMillis();
        printResponseLog((HttpServletResponse) response, requestWrapper, responseWrapper, start, end);

        MDC.clear();

    }

    private Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private Map getHeaders(HttpServletResponse response) {
        Map headerMap = new HashMap<>();

        Iterator headerArray = response.getHeaderNames().iterator();
        while (headerArray.hasNext()) {
            String headerName = (String) headerArray.next();
            headerMap.put(headerName, response.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        try {
            wrapper.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // Do-Noting
            log.info("wrapper character encodeing settting fail");
        }

        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.setCharacterEncoding("UTF-8");

            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }

    private void printRequestLog(HttpServletRequest request,
                                 ContentCachingRequestWrapper requestWrapper,
                                 ContentCachingResponseWrapper responseWrapper) throws IOException {
        log.info("" +
                        "[REQUEST] {} - {}, " +
                        "Headers : {}, " +
                        "Request : {}, ",
                request.getMethod(),
                request.getRequestURI(),
                getHeaders(request),
                getRequestBody(requestWrapper));
    }

    private void printResponseLog(HttpServletResponse response,
                                  ContentCachingRequestWrapper requestWrapper,
                                  ContentCachingResponseWrapper responseWrapper,
                                  long start,
                                  long end) throws IOException {
        log.info("" +
                        "[RESPONSE] status : {} - {}, " +
                        "Headers : {}, " +
                        "Response : {}, ",
                responseWrapper.getStatus(),
                (end - start) / 1000.0,
                getHeaders(response),
                getResponseBody(responseWrapper));
    }
}
