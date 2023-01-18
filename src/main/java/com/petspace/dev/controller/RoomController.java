package com.petspace.dev.controller;

import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/app/room")
    public BaseResponse<RoomDetailResponseDto> getRoomDetail(@RequestParam("id") Long roomId){
        RoomDetailResponseDto roomDetailResponseDto = roomService.getRoomDetail(roomId);

        return new BaseResponse(roomDetailResponseDto);
    }

    // TODO
//    @PostMapping("/api/room")
//    public int createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto){
//        return roomService.createRoom(roomCreateRequestDto);
//    }

}
