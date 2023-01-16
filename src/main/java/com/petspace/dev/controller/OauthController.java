package com.petspace.dev.controller;

import com.petspace.dev.dto.oauth.OauthRequestDto;
import com.petspace.dev.dto.oauth.OauthResponseDto;
import com.petspace.dev.service.OauthService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/{provider}")
    public BaseResponse<OauthResponseDto> OauthLoginRequest(@PathVariable String provider,
                                                            @RequestBody OauthRequestDto requestDto) {
        log.info("provider={}, accessToken={}", provider, requestDto.getAccessToken());
        OauthResponseDto oauthResponseDto = oauthService.login(provider, requestDto);
        return new BaseResponse<>(oauthResponseDto);
    }
}
