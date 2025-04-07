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

    @Value("${KAKAO_CLIENT_ID}")
    private String clientId;

    @Value("${KAKAO_REDIRECT_URI}")
    private String redirectUri;

    @Value("${SECURITY_JWT_SECRET}")
    private String jwtSecret;

}