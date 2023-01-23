package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.favorite.FavoriteRegionsResponseDto;
import com.petspace.dev.service.FavoriteService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/favorites")
    public BaseResponse<FavoriteRegionsResponseDto> showMainPage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getId();
        log.info("user={}", userId);
        FavoriteRegionsResponseDto regionResponseDto = favoriteService.showRegions(userId);
        return new BaseResponse<>(regionResponseDto);
    }
}
