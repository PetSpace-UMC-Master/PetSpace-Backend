package com.petspace.dev.service;

import com.petspace.dev.domain.user.User;
import com.petspace.dev.dto.auth.LoginTokenReissueRequestDto;
import com.petspace.dev.dto.auth.LoginTokenResponseDto;
import com.petspace.dev.dto.user.UserCheckEmailResponseDto;
import com.petspace.dev.dto.user.UserJoinRequestDto;
import com.petspace.dev.dto.user.UserLoginRequestDto;
import com.petspace.dev.dto.user.UserResponseDto;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.exception.ReissueException;
import com.petspace.dev.util.exception.UserException;
import com.petspace.dev.util.jwt.JwtProvider;
import com.petspace.dev.util.jwt.Token;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    private final AwsS3Uploader awsS3Uploader;
    private final UserRepository userRepository;


    @Value("${default.image.url}")
    private String defaultProfileImage;

    public UserResponseDto join(UserJoinRequestDto joinRequestDto) {

        validateSignupDto(joinRequestDto);

        User user = joinRequestDto.toEntity(defaultProfileImage);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.encodePassword(encodedPassword);
        userRepository.save(user);
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .birth(user.getBirth())
                .build();
    }

    public UserResponseDto updateProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NONE_USER));
        String profileUrl = awsS3Uploader.upload(image, "profile");

        user.updateProfileImage(profileUrl);

        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .birth(user.getBirth())
                .build();
    }

    public LoginTokenResponseDto login(UserLoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDto.getPassword(), u.getPassword()))
                .orElseThrow(() -> new UserException(INVALID_EMAIL_OR_PASSWORD));

        String email = user.getEmail();
        Token token = jwtProvider.createToken(email);

        redisService.save(email, token);

        return LoginTokenResponseDto.builder()
                .email(user.getEmail())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public LoginTokenResponseDto reissueRefreshToken(LoginTokenReissueRequestDto reissueRequestDto) {
        String oldAccessToken = reissueRequestDto.getAccessToken();
        String oldRefreshToken = reissueRequestDto.getRefreshToken();
        String email = extractUserEmailFromRequest(oldAccessToken, oldRefreshToken);

        Token token = jwtProvider.createToken(email);
        redisService.save(email, token);

        return LoginTokenResponseDto.builder()
                .email(email)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    public UserCheckEmailResponseDto checkEmailDuplicate(String email) {
        boolean isAvailable = validateDuplicateEmail(email);
        return UserCheckEmailResponseDto.builder()
                .email(email)
                .isAvailable(isAvailable)
                .build();
    }

    private void validateSignupDto(UserJoinRequestDto joinRequestDto) {
        validateDuplicateEmail(joinRequestDto.getEmail());
        validateCheckedPassword(joinRequestDto.getPassword(), joinRequestDto.getCheckedPassword());
    }

    private boolean validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserException(DUPLICATED_EMAIL);
        }
        return true;
    }

    private void validateCheckedPassword(String password, String checkedPassword) {
        if (!password.equals(checkedPassword)) {
            throw new UserException(INVALID_CHECKED_PASSWORD);
        }
    }

    private String extractUserEmailFromRequest(String oldAccessToken, String oldRefreshToken) {
        String email;

        // 잘못된 AccessToken을 요청하는 경우
        try {
            email = jwtProvider.getPayload(oldAccessToken);
        } catch (Exception e) {
            throw new ReissueException(INVALID_REQUEST);
        }

        // 요청한 Refresh 토큰이 만료된 경우
        if (redisService.getValue(email) == null) {
            throw new ReissueException(EXPIRED_REFRESH_TOKEN);
        }

        // 잘못된 Refresh 토큰을 요청하는 경우
        if (!oldRefreshToken.equals(redisService.getValue(email))) {
            throw new ReissueException(INVALID_REFRESH_TOKEN);
        }
        return email;
    }
}
