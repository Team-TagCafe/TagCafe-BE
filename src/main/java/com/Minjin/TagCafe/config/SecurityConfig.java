package com.Minjin.TagCafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/oauth/kakao/**").permitAll() // 카카오 로그인 엔드포인트는 인증 없이 접근 가능
                        .anyRequest().authenticated()
                )
                .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}