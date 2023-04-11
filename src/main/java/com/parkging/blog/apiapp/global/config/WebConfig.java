package com.parkging.blog.apiapp.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    /**
//     * CORS 전역 설정
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                // 허용 출처
//                .allowedOrigins("http://localhost:3000")
//                // 허용 HTTP method
//                .allowedMethods("GET", "POST", "PATCH", "DELETE")
//                // 모든 헤더값 참조 허용
//                .exposedHeaders("*")
//                // 쿠키 인증 요청 허용
//                .allowCredentials(true)
//                // pre-flight 리퀘스트 캐싱 시간
//                .maxAge(3000);
//    }
}
