package com.petspace.dev.controller;

import com.petspace.dev.dto.room.RoomListResponseDto;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public BaseResponse<List<RoomListResponseDto>> get(@RequestParam Optional<Integer> page,
                                                       @RequestParam Optional<String> sortBy,
                                                       @RequestParam Optional<String> order,
                                                       @RequestParam Optional<Long> categoryId) {
        if (!categoryId.isEmpty()) {
            return new BaseResponse<>(roomService.findAllDescByCategory(page, sortBy, order, categoryId));
        }
        return new BaseResponse<>(roomService.findAllDesc(page, sortBy, order));
    }
}