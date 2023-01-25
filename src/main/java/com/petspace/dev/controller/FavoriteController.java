package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.favorite.FavoriteClickResponseDto;
import com.petspace.dev.dto.favorite.FavoriteResponseDto;
import com.petspace.dev.service.FavoriteService;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.input.room.RegionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/favorites")
    public BaseResponse<List<FavoriteResponseDto>> showFavorites(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                 @RequestParam RegionType region) {
        Long userId = principalDetails.getId();
        List<FavoriteResponseDto> responseDtos = favoriteService.showFavoritesByRegion(userId, region.getKorRegionName());
        return new BaseResponse<>(responseDtos);
    }

    @PostMapping("/favorites")
    public BaseResponse<FavoriteClickResponseDto> addFavorite(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @RequestParam Long roomId) {
        Long userId = principalDetails.getId();
        log.info("user=[{}][{}]", principalDetails.getId(), principalDetails.getUsername());
        FavoriteClickResponseDto roomResponseDto = favoriteService.clickFavorite(userId, roomId);
        return new BaseResponse<>(roomResponseDto);
    }
}
