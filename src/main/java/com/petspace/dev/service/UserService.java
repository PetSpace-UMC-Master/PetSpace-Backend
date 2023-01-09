package com.petspace.dev.service;

import com.petspace.dev.util.jwt.JwtProvider;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.UserLoginResponseDto;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public UserLoginResponseDto login(User loginUser) {
        User user = userRepository.findByEmail(loginUser.getEmail())
                .filter(u -> u.getPassword().equals(u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 잘못되었습니다."));
        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken();
        log.info("accessToken={}, refreshToken={}", accessToken, refreshToken);
        return UserLoginResponseDto.builder()
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
