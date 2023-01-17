package com.petspace.dev.controller;

import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.dto.review.ReviewCreateResponseDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.petspace.dev.util.BaseResponseStatus.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Review Post", description = "Review Post API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2020", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2021", description = "해당 예약이 존재하지 않습니다."),
            @ApiResponse(responseCode = "2022", description = "score를 입력해주세요.")
    })
    @PostMapping("/create")
    public BaseResponse createReview(@RequestParam("userId") Long userId,
                                     @RequestParam("reservationId") Long reservationId,
                                     @Valid @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {
        ReviewCreateResponseDto createResponseDto = reviewService.save(userId, reservationId, reviewCreateRequestDto);
        log.info("score={}", reviewCreateRequestDto.getScore());


        return new BaseResponse<>(createResponseDto);
    }
}

