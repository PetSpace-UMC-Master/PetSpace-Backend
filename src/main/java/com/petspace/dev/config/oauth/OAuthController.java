package com.petspace.dev.config.oauth;

import com.petspace.dev.config.oauth.dto.UserOAuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{provider}")
    public ResponseEntity<UserOAuthResponseDto> KakaoAuthRequest_PathVariable(@PathVariable String provider, @RequestParam String code) {
        log.info("provider={}, code={}", provider, code);
        UserOAuthResponseDto loginResponseDto = oAuthService.login(provider, code);
        return ResponseEntity.ok(loginResponseDto);
    }
}
