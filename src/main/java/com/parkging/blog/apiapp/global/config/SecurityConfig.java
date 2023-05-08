package com.parkging.blog.apiapp.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkging.blog.apiapp.domain.member.service.MemberService;
import com.parkging.blog.apiapp.global.config.cors.CorsConfig;
import com.parkging.blog.apiapp.global.exception.ErrorMessageUtil;
import com.parkging.blog.apiapp.global.filter.ExceptionHandlerFilter;
import com.parkging.blog.apiapp.global.filter.LogFilter;
import com.parkging.blog.apiapp.global.jwt.*;
import com.parkging.blog.apiapp.global.jwt.filter.JwtAuthenticationFilter;
import com.parkging.blog.apiapp.global.jwt.filter.JwtAuthorizationFilter;
import com.parkging.blog.apiapp.global.jwt.filter.JwtRevalidationFilter;
import com.parkging.blog.apiapp.global.jwt.util.JwtSecretKeyUtil;
import com.parkging.blog.apiapp.global.oauth.handler.OAuth2LoginFailureHandler;
import com.parkging.blog.apiapp.global.oauth.handler.OAuth2LoginSuccessHandler;
import com.parkging.blog.apiapp.global.oauth.service.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final ErrorMessageUtil errorMessageUtil;
    private final CorsConfig corsConfig;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final JwtSecretKeyUtil jwtSecretKeyUtil;
    private final PrincipalOauth2UserService principalOauth2UserService;

    // Oauth filters
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Exception 필터 추가
        http.addFilterBefore(new ExceptionHandlerFilter(errorMessageUtil, memberService), SecurityContextPersistenceFilter.class);
        //LOG 필터 추가
        http.addFilterBefore(new LogFilter(), ExceptionHandlerFilter.class);

        // 세션 미사용하므로 csrf 비활성화, Stateless 세팅
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilterDsl()) //커스텀 필터 적용
                .and()
                .authorizeRequests()
//                .antMatchers("/members/**").authenticated()  // 인증 되면 접속
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 인가 되면 접속
                .anyRequest().permitAll()
                .and()
                // Oauth Setting
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(principalOauth2UserService)

        ;
        return http.build();
    }

    public class CustomFilterDsl extends AbstractHttpConfigurer<CustomFilterDsl, HttpSecurity> {

        @Override
        public void configure(HttpSecurity http) throws Exception {

//            //Exception Filters
//            ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter(errorMessageUtil, memberService);;

            //JWT filters
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, objectMapper, jwtSecretKeyUtil);
            JwtRevalidationFilter jwtRevalidationFilter = new JwtRevalidationFilter(authenticationManager, memberService, jwtSecretKeyUtil);
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberService, jwtSecretKeyUtil);

            jwtAuthenticationFilter.setUsernameParameter(JwtProperties.JWT_USERNAME_PARAMETER);
            jwtRevalidationFilter.setUsernameParameter(JwtProperties.JWT_USERNAME_PARAMETER);
            jwtRevalidationFilter.setFilterProcessesUrl(JwtProperties.REFRESH_TOKEN_URL);

            http
                    //Exception Filter
//                    .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)

                    //JWT filters
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(jwtAuthenticationFilter)
                    .addFilter(jwtRevalidationFilter)
                    .addFilter(jwtAuthorizationFilter)
            ;
        }
    }

}
