package com.petspace.dev.service;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserLoginResponseDto;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.exception.UserException;
import com.petspace.dev.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(User user) {

        validateDuplicateEmail(user.getEmail());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.encodePassword(encodedPassword);
        userRepository.save(user);
        return user.getId();
    }

    public UserLoginResponseDto login(User loginUser) {
        String password = loginUser.getPassword();
        User user = userRepository.findByEmail(loginUser.getEmail())
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new UserException(INVALID_EMAIL_OR_PASSWORD));

        String accessToken = jwtProvider.createAccessToken(user.getEmail());
        String refreshToken = jwtProvider.createRefreshToken();
        log.info("accessToken={}, refreshToken={}", accessToken, refreshToken);
        return UserLoginResponseDto.builder()
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public BaseResponse<Object> checkEmailDuplicate(String email) {
        validateDuplicateEmail(email);
        return new BaseResponse<>(NON_DUPLICATE_EMAIL);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(DUPLICATED_EMAIL);
        }
    }
}
