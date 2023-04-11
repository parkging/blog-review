package com.parkging.blog.apiapp.global.config;

import com.parkging.blog.apiapp.global.filter.TempFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    /*todo 로그 필터 추가*/

    @Bean
    public FilterRegistrationBean<TempFilter> tempFilter() {
        FilterRegistrationBean<TempFilter> bean = new FilterRegistrationBean<>(new TempFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }

}
