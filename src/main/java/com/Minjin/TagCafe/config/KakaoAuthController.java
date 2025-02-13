package com.Minjin.TagCafe.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthController.class);

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public RedirectView kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoConfig.getClientId()
                + "&redirect_uri=" + kakaoConfig.getRedirectUri()
                + "&scope=profile_nickname,profile_image,account_email";

        return new RedirectView(kakaoAuthUrl);
    }

    @GetMapping("/callback")
    public RedirectView kakaoCallback(@RequestParam("code") String code) {
        logger.info("🔍 카카오 로그인 콜백 호출됨, Authorization Code: {}", code);

        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // 1. HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 요청 Body 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId()); // ✅ 수정
        params.add("redirect_uri", kakaoConfig.getRedirectUri()); // ✅ 수정
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (tokenResponse.getBody() == null) {
            logger.error("❌ 카카오 토큰 응답이 없습니다.");
            return new RedirectView("http://localhost:3000/error?message=카카오 토큰 발급 실패");
        }

        Map<String, Object> tokenMap = tokenResponse.getBody();
        String accessToken = (String) tokenMap.get("access_token");

        if (accessToken == null) {
            logger.error("❌ 액세스 토큰이 없습니다. 응답: {}", tokenResponse.getBody());
            return new RedirectView("http://localhost:3000/error?message=액세스 토큰 발급 실패");
        }

        logger.info("✅ 카카오 액세스 토큰 발급 완료: {}", accessToken);

        // ✅ 4. 사용자 정보 요청
        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        if (userInfoResponse.getBody() == null) {
            logger.error("❌ 카카오 사용자 정보 응답이 없습니다.");
            return new RedirectView("http://localhost:3000/error?message=카카오 사용자 정보 가져오기 실패");
        }

        // ✅ 5. 사용자 정보 추출
        Map<String, Object> userInfo = userInfoResponse.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");

        if (kakaoAccount == null) {
            return new RedirectView("http://localhost:3000/error?message=카카오 계정 정보 없음");
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile != null ? (String) profile.get("nickname") : "Unknown";
        String encodedNickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);

        String email = (String) kakaoAccount.get("email");

        logger.info("✅ 카카오 로그인 성공! 닉네임: {}, 이메일: {}", nickname, email);

        // ✅ 6. 프론트엔드로 리다이렉트
        return new RedirectView("http://localhost:3000/home?nickname=" + encodedNickname + "&email=" + email);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);

        return ResponseEntity.ok("카카오 사용자 정보: " + response.getBody());
    }
}