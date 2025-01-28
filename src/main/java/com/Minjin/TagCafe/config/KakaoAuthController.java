package com.Minjin.TagCafe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        String clientId = EnvConfig.get("KAKAO_REST_API_KEY"); // 카카오 REST API 키
        String redirectUri = "http://localhost:8080/oauth/kakao/callback";

        // 카카오 토큰 요청 URL
        String tokenRequestUrl = "https://kauth.kakao.com/oauth/token"
                + "?grant_type=authorization_code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&code=" + code;

        ResponseEntity<String> response = restTemplate.postForEntity(tokenRequestUrl, null, String.class);

        return ResponseEntity.ok("카카오 로그인 성공! 응답: " + response.getBody());
    }
}
