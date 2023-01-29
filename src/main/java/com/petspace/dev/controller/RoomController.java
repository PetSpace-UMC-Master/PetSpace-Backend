package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.favorite.FavoriteClickResponseDto;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.service.FavoriteService;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final FavoriteService favoriteService;
    private final RoomService roomService;

    @PostMapping("/rooms/{roomId}/favorites")
    public BaseResponse<FavoriteClickResponseDto> addFavorite(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                              @PathVariable Long roomId) {
        Long userId = principalDetails.getId();
        log.info("user=[{}][{}]", principalDetails.getId(), principalDetails.getUsername());
        FavoriteClickResponseDto roomResponseDto = favoriteService.clickFavorite(userId, roomId);
        return new BaseResponse<>(roomResponseDto);
    }

    @Operation(summary = "RoomDetail Get", description = "RoomDetail Get API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "존재하지 않는 숙소 정보입니다.")
    })
    @GetMapping("/room/{id}")
    public BaseResponse<RoomDetailResponseDto> getRoomDetail(@PathVariable("id") Long roomId){
        RoomDetailResponseDto roomDetailResponseDto = roomService.getRoomDetail(roomId);

        return new BaseResponse(roomDetailResponseDto);
    }

}
