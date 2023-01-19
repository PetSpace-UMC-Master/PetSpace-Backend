package com.petspace.dev.controller;

import com.petspace.dev.util.input.room.SortBy;
import com.petspace.dev.dto.room.RoomListResponseDto;
import com.petspace.dev.service.RoomService;
import com.petspace.dev.util.BaseResponse;
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
                                                       @RequestParam Optional<SortBy> sortBy,
                                                       @RequestParam Optional<Long> categoryId) {
        //TODO: formal validation
        if (!categoryId.isEmpty()) {
            return new BaseResponse<>(roomService.findAllDescByCategory(page, sortBy, categoryId));
        }
        return new BaseResponse<>(roomService.findAllDesc(page, sortBy));
    }
}