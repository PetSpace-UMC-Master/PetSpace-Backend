package com.petspace.dev.config.oauth;

import com.petspace.dev.config.oauth.dto.OAuthAttributes;
import com.petspace.dev.config.oauth.dto.OAuthTokenResponse;
import com.petspace.dev.config.oauth.jwt.JwtProvider;
import com.petspace.dev.domain.User;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private static final String BEARER_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public void login(String providerName, String code) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        log.info("provider name={}, id={}", provider.getClientName(), provider.getClientId());
        User user = getUserProfile(provider, code);
        log.info("user nickname={}, email={}, oauthProvider={}", user.getNickname(), user.getEmail(), user.getOauthProvider());
        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtProvider.createRefreshToken();

        log.info("accessToken={}, refreshToken={}", accessToken, refreshToken);
        // 이제 이렇게 발급 받은 토큰을 서버(Redis)단에서 저장하고, 프론트단으로 넘겨주면됨
    }

    /**
     * 회원정보를 토대로 회원 추출
     */
    private User getUserProfile(ClientRegistration provider, String code) {
        OAuthTokenResponse token = getToken(provider, code);
        Map<String, Object> userAttributes = getUserAttributes(provider, token);
        User extract = OAuthAttributes.extract(provider.getClientName(), userAttributes);
        log.info("user=[{}][{}][{}]", extract.getEmail(), extract.getNickname(), extract.getOauthProvider());
        return extract;
    }

    /**
     *  token 생성
     */
    private OAuthTokenResponse getToken(ClientRegistration provider, String code) {
        log.info("provider TokenUri={}", provider.getProviderDetails().getTokenUri());
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                    log.info("header={}", header);
                })
                .bodyValue(tokenRequest(provider, code))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    /**
     * token 발급 요청
     */
    private MultiValueMap<String, String> tokenRequest(ClientRegistration provider, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_id", provider.getClientId());
        log.info("redirectUri={}", provider.getRedirectUri());
        return formData;
    }

    /**
     * token을 토대로 회원정보 요청
     */
    private Map<String, Object> getUserAttributes(ClientRegistration provider, OAuthTokenResponse tokenResponse) {
        log.info("userInfoUri = {}", provider.getProviderDetails().getUserInfoEndpoint().getUri());
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header ->
                        header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
