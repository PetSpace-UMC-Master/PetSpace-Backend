package com.petspace.dev.controller;

import com.petspace.dev.domain.Room;
import com.petspace.dev.dto.room.RoomCreateRequestDto;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/api/room")
    public BaseResponse<RoomDetailResponseDto> getRoomDetail(@RequestParam("id") Long roomId){
        System.out.println("roomId !!! : " +roomId);
        RoomDetailResponseDto roomDetailResponseDto = roomService.getRoomDetail(roomId);

        return new BaseResponse(roomDetailResponseDto);
    }

    // TODO
//    @PostMapping("/api/room")
//    public int createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto){
//        return roomService.createRoom(roomCreateRequestDto);
//    }

}
