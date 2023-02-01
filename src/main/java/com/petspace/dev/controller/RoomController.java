package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.favorite.FavoriteClickResponseDto;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.dto.room.RoomFacilityResponseDto;
import com.petspace.dev.service.FavoriteService;
import com.petspace.dev.util.input.room.CategoryType;
import com.petspace.dev.util.input.room.SortBy;
import com.petspace.dev.dto.room.RoomListResponseDto;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app")
public class RoomController {
    private final FavoriteService favoriteService;
    private final RoomService roomService;

    @GetMapping("/rooms")
    public BaseResponse<List<RoomListResponseDto>> get(@RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<SortBy> sortBy,
                                                       @RequestParam Optional<CategoryType> categoryType) {
        if (!categoryType.isEmpty()) {
            return new BaseResponse<>(roomService.findAllDescByCategory(page, sortBy, categoryType.get()));
        }
        return new BaseResponse<>(roomService.findAllDesc(page, sortBy));
    }

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
    @GetMapping("/rooms/{roomId}")
    public BaseResponse<RoomDetailResponseDto> getRoomDetail(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("roomId") Long roomId){

        RoomDetailResponseDto roomDetailResponseDto;

        if(principalDetails == null){
            roomDetailResponseDto = roomService.getRoomDetail(roomId, null);
        }else{
            Long userId = principalDetails.getId();
            roomDetailResponseDto = roomService.getRoomDetail(roomId, userId);
        }

        return new BaseResponse(roomDetailResponseDto);

    }

    @Operation(summary = "AllFacilities Get", description = "AllFacilities Get API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "존재하지 않는 숙소 정보입니다.")
    })
    @GetMapping("/room/{roomId}/facilities")
    public BaseResponse<RoomFacilityResponseDto> getRoomFacilities(@PathVariable("roomId") Long roomId){

        RoomFacilityResponseDto roomFacilitiesDto = roomService.getRoomFacilities(roomId);
        return new BaseResponse(roomFacilitiesDto);

    }
}
