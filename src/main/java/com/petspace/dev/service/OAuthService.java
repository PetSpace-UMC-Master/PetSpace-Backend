package com.petspace.dev.service;

import com.petspace.dev.oauth.OAuthAttributes;
import com.petspace.dev.dto.oauth.OAuthRequestDto;
import com.petspace.dev.dto.oauth.OAuthResponseDto;
import com.petspace.dev.domain.User;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public OAuthResponseDto login(String providerName, OAuthRequestDto requestDto) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        log.info("provider name={}, id={}", provider.getClientName(), provider.getClientId());
        User user = getUserProfile(provider, requestDto);
        log.info("user nickname={}, email={}, oauthProvider={}", user.getNickname(), user.getEmail(), user.getOauthProvider());
        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtProvider.createRefreshToken();

        log.info("accessToken={}, refreshToken={}", accessToken, refreshToken);

        return OAuthResponseDto.builder()
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 회원정보를 토대로 회원 추출
     */
    private User getUserProfile(ClientRegistration provider, OAuthRequestDto requestDto) {
        Map<String, Object> userAttributes = getUserAttributes(provider, requestDto);
        User extract = OAuthAttributes.extract(provider.getClientName(), userAttributes);
        log.info("user=[{}][{}][{}]", extract.getEmail(), extract.getNickname(), extract.getOauthProvider());
        return extract;
    }

    /**
     * token을 토대로 회원정보 요청
     */
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OAuthRequestDto requestDto) {
        log.info("userInfoUri = {}", provider.getProviderDetails().getUserInfoEndpoint().getUri());
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header ->
                        header.setBearerAuth(requestDto.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
