package com.Minjin.TagCafe.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class KakaoConfig {
    private static final Logger logger = LoggerFactory.getLogger(KakaoConfig.class);

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

}