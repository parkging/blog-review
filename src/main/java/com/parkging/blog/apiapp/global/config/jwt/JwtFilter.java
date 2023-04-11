package com.parkging.blog.apiapp.global.config.jwt;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

//        //id, pw가 정상적으로 들어오면 로그인이 완료되면서 토큰을 생성 후 응답
//        // 토큰이 넘어오면 검증
//
//        System.out.println("JwtFilter");
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        res.setCharacterEncoding("UTF-8");
//
//        if(req.getMethod().equals("POST")) {
//            String headerAuth = req.getHeader("Authorization");
//            System.out.println("headerAuth = " + headerAuth);
//
//            if(headerAuth.equals("JWT")) {
//                chain.doFilter(req, res);
//            } else {
//                PrintWriter out = res.getWriter();
//                out.println("no Auth");
//            }
//        }

    }
}
