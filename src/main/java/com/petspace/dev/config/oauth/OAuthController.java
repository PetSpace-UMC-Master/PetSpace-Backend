package com.petspace.dev.config.oauth;

import com.petspace.dev.config.oauth.dto.OAuthRequestDto;
import com.petspace.dev.config.oauth.dto.OAuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/{provider}")
    public ResponseEntity<OAuthResponseDto> OauthLoginRequest(@PathVariable String provider,
                                                              @RequestBody OAuthRequestDto requestDto) {
        log.info("provider={}, accessToken={}", provider, requestDto.getAccessToken());
        OAuthResponseDto oAuthResponseDto = oAuthService.login(provider, requestDto);
        return ResponseEntity.ok(oAuthResponseDto);
    }
}
