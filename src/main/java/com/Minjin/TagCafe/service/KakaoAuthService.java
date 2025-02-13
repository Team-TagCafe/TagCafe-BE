package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.config.KakaoConfig;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);
    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 카카오 로그인 후 액세스 토큰을 요청
     */
    public String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId());
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (response.getBody() == null || response.getBody().get("access_token") == null) {
            logger.error("❌ 카카오 액세스 토큰 발급 실패: {}", response);
            throw new RuntimeException("카카오 액세스 토큰을 발급받을 수 없습니다.");
        }

        String accessToken = (String) response.getBody().get("access_token");
        logger.info("✅ 카카오 액세스 토큰: {}", accessToken);
        return accessToken;
    }

    /**
     * 카카오 사용자 정보 가져오기
     */
    public Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        if (response.getBody() == null) {
            logger.error("❌ 카카오 사용자 정보를 가져올 수 없습니다.");
            throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
        }

        logger.info("✅ 카카오 사용자 정보: {}", response.getBody());
        return response.getBody();
    }
}