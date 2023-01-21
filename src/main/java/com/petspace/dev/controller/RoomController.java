package com.petspace.dev.controller;

import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(summary = "RoomDetail Get", description = "RoomDetail Get API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "존재하지 않는 숙소 정보입니다.")
    })
    @GetMapping("/app/room/{id}")
    public BaseResponse<RoomDetailResponseDto> getRoomDetail(@PathVariable("id") Long roomId){
        RoomDetailResponseDto roomDetailResponseDto = roomService.getRoomDetail(roomId);

        return new BaseResponse(roomDetailResponseDto);
    }

}
