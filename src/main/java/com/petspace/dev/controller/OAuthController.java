package com.petspace.dev.controller;

import com.petspace.dev.dto.oauth.OAuthRequestDto;
import com.petspace.dev.dto.oauth.OAuthResponseDto;
import com.petspace.dev.service.OAuthService;
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
