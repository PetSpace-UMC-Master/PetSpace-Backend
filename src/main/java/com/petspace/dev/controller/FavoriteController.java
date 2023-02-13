package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.favorite.FavoriteClickResponseDto;
import com.petspace.dev.dto.favorite.FavoritesSliceResponseDto;
import com.petspace.dev.service.FavoriteService;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.input.room.RegionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Favorite Get", description = "Favorite GET API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/favorites")
    public BaseResponse<FavoritesSliceResponseDto> showFavorites(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                 @RequestParam RegionType region,
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        log.info("input region={}", region.getKorRegionName());
        Long userId = principalDetails.getId();
        PageRequest pageRequest = PageRequest.of(page, size);
        log.info("favorite 통신 [{}][{}]", region.getKorRegionName(), page);
        FavoritesSliceResponseDto responseDto = favoriteService.showFavoritesSliceByRegion(userId, region.getKorRegionName(), pageRequest);
        log.info("favorite 통신 성공!!");
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Favorite POST", description = "Favorite POST API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/favorites")
    public BaseResponse<FavoriteClickResponseDto> addFavorite(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @RequestParam Long roomId) {
        Long userId = principalDetails.getId();
        log.info("user=[{}][{}]", principalDetails.getId(), principalDetails.getUsername());
        FavoriteClickResponseDto responseDto = favoriteService.clickFavorite(userId, roomId);
        return new BaseResponse<>(responseDto);
    }
}
