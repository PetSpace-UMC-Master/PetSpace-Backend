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
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2013", description = "존재하지 않는 회원입니다."),
            @ApiResponse(responseCode = "2098", description = "Request Parameter가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2099", description = "Request Parameter의 유형이 불일치합니다.")
    })
    @GetMapping("/favorites")
    public BaseResponse<FavoritesSliceResponseDto> showFavorites(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                 @RequestParam RegionType region,
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        Long userId = principalDetails.getId();
        PageRequest pageRequest = PageRequest.of(page, size);
        FavoritesSliceResponseDto responseDto = favoriteService.showFavoritesSliceByRegion(userId, region.getKorRegionName(), pageRequest);
        return new BaseResponse<>(responseDto);
    }


    @Operation(summary = "Favorite POST", description = "Favorite POST API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2013", description = "존재하지 않는 회원입니다."),
            @ApiResponse(responseCode = "2030", description = "존재하지 않는 숙소입니다."),
            @ApiResponse(responseCode = "2098", description = "Request Parameter가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2099", description = "Request Parameter의 유형이 불일치합니다.")
    })
    @PostMapping("/favorites")
    public BaseResponse<FavoriteClickResponseDto> addFavorite(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @RequestParam Long roomId) {
        Long userId = principalDetails.getId();
        log.info("user=[{}][{}]", principalDetails.getId(), principalDetails.getUsername());
        FavoriteClickResponseDto roomResponseDto = favoriteService.clickFavorite(userId, roomId);
        return new BaseResponse<>(roomResponseDto);
    }
}
