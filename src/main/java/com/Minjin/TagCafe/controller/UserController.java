package com.Minjin.TagCafe.controller;

import com.Minjin.TagCafe.dto.NicknameRequest;
import com.Minjin.TagCafe.entity.User;
import com.Minjin.TagCafe.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "User", description = "사용자 정보 관리 및 탈퇴 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://tagcafe.site", allowCredentials = "true")
public class UserController {
    private final UserRepository userRepository;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Operation(summary = "닉네임 변경", description = "사용자의 이메일을 기반으로 닉네임을 변경합니다.")
    @PutMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestBody NicknameRequest request) {
        String email = request.getEmail();
        String newNickname = request.getNewNickname();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNickname(newNickname);
            userRepository.save(user);
            return ResponseEntity.ok("닉네임이 성공적으로 반영되었습니다");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다");
    }

    @Operation(summary = "회원 탈퇴", description = "이메일로 사용자를 삭제하고 세션 및 쿠키를 정리합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam("email") String email, HttpServletRequest req, HttpServletResponse res) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());

            // ✅ 세션 무효화
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // ✅ 모든 쿠키 삭제
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setValue(null);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    res.addCookie(cookie);
                }
            }

            // ✅ 카카오 로그아웃 URL 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 탈퇴 성공");
            response.put("logoutUrl", "https://kauth.kakao.com/oauth/logout?client_id=" + kakaoClientId + "&logout_redirect_uri=https://tagcafe.site");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "사용자를 찾을 수 없습니다."));
        }
    }

    @Operation(summary = "유저 ID 조회", description = "이메일을 기반으로 유저 ID를 조회합니다.")
    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(@RequestParam("email") String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
