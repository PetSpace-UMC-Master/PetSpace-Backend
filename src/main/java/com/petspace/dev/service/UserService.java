package com.petspace.dev.service;

import com.petspace.dev.util.jwt.JwtProvider;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserLoginResponseDto;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.encodePassword(encodedPassword);
        userRepository.save(user);
        return user.getId();
    }

    public UserLoginResponseDto login(User loginUser) {
        String password = loginUser.getPassword();
        User user = userRepository.findByEmail(loginUser.getEmail())
                .filter(u -> u.getPassword().equals(u.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 잘못되었습니다."));

        // TODO BaseReponse에 담아서 Exception 처리?
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

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
