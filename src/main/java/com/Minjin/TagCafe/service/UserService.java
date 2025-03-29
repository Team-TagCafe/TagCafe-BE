package com.Minjin.TagCafe.service;

import com.Minjin.TagCafe.entity.User;
import com.Minjin.TagCafe.repository.UserRepository;
import com.Minjin.TagCafe.util.JwtUtil;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String processUserLogin(String nickname, String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User(nickname, email);
            userRepository.save(user);
        }

        // ✅ JWT 생성 후 반환
        return jwtUtil.generateToken(user.getEmail());
    }

    // 이메일로 닉네임 조회
    public String getNicknameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getNickname)
                .orElse("알 수 없음");
    }

}