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

    private String clientId;

    private String redirectUri;

    private String jwtSecret;

    @PostConstruct
    public void init() {
        this.clientId = System.getProperty("KAKAO_CLIENT_ID");
        this.redirectUri = System.getProperty("KAKAO_REDIRECT_URI");
        this.jwtSecret = System.getProperty("SECURITY_JWT_SECRET");

        logger.info("✅ KakaoConfig initialized: clientId={}, redirectUri={}", clientId, redirectUri);
    }

}