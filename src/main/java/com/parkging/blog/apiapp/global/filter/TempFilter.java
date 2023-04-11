package com.parkging.blog.apiapp.global.filter;

import javax.servlet.*;
import java.io.IOException;

public class TempFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("TempFilter");
        chain.doFilter(request, response);
    }
}
