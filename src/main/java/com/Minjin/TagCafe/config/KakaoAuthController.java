package com.Minjin.TagCafe.config;

import com.Minjin.TagCafe.entity.User;
import com.Minjin.TagCafe.repository.UserRepository;
import com.Minjin.TagCafe.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Optional;

@Tag(name = "Auth", description = "카카오 로그인 및 사용자 인증 관련 API")
@RestController
@RequestMapping("/oauth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthController.class);

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Operation(summary = "카카오 로그인 시작", description = "카카오 로그인 인증 페이지로 리디렉션합니다.")
    @GetMapping("/login")
    public RedirectView kakaoLogin() {
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?response_type=code"
                + "&client_id=" + kakaoConfig.getClientId()
                + "&redirect_uri=" + kakaoConfig.getRedirectUri()
                + "&scope=profile_nickname,profile_image,account_email";

        return new RedirectView(kakaoAuthUrl);
    }

    @Operation(summary = "카카오 로그인 콜백", description = "카카오 인증 후 사용자 정보를 받아옵니다.")
    @GetMapping("/callback")
    public RedirectView kakaoCallback(@RequestParam(name="code") String code) {

        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // 1. HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. 요청 Body 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getClientId());
        params.add("redirect_uri", kakaoConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);

        if (tokenResponse.getBody() == null) {
            return new RedirectView("https://tagcafe.site/error?message=카카오 토큰 발급 실패");
        }

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        if (accessToken == null) {
            return new RedirectView("https://tagcafe.site/error?message=액세스 토큰 발급 실패");
        }

        // ✅ 4. 사용자 정보 요청
        headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        if (userInfoResponse.getBody() == null) {
            return new RedirectView("https://tagcafe.site/error?message=카카오 사용자 정보 가져오기 실패");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfoResponse.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoNickname = profile != null ? (String) profile.get("nickname") : "Unknown";
        String email = (String) kakaoAccount.get("email");

        logger.info("✅ 카카오 로그인 성공! 닉네임: {}, 이메일: {}", kakaoNickname, email);

        // ✅ 6. DB에서 사용자 확인 및 저장
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();

            // ✅ DB에 저장된 최신 닉네임을 사용
            String storedNickname = user.getNickname();
            logger.info("🔄 로그인 닉네임 설정: DB닉네임={}, 카카오닉네임={}", storedNickname, kakaoNickname);
        } else {
            // 신규 유저라면 카카오 닉네임을 저장
            user = new User(kakaoNickname, email);
            userRepository.save(user);
            logger.info("🆕 신규 사용자 등록: {}", email);
        }

        // ✅ 7. JWT 발급
        String jwtToken = jwtUtil.generateToken(user.getEmail());

        // ✅ 8. 프론트엔드로 리다이렉트 (닉네임은 DB에서 가져온 최신값)
        return new RedirectView("https://tagcafe.site/home?nickname=" + URLEncoder.encode(user.getNickname(), StandardCharsets.UTF_8)
                + "&email=" + email
                + "&token=" + jwtToken);
    }

    @Operation(summary = "로그인한 사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 반환합니다.")
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