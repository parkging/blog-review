package com.parkging.blog.apiapp.global.config;

import com.parkging.blog.apiapp.global.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Bean
//    public FilterRegistrationBean<LogFilter> logFilter() {
//        FilterRegistrationBean<LogFilter> bean = new FilterRegistrationBean<>(new LogFilter());
//        bean.addUrlPatterns("/*");
//        bean.setOrder(0);
//        return bean;
//    }

}
