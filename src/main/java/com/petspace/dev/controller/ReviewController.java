package com.petspace.dev.controller;

import com.petspace.dev.dto.review.ReviewListResponseDto;
import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.dto.review.ReviewCreateResponseDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/app")
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
    @PostMapping("/reviews")
    public BaseResponse createReview(@RequestParam("userId") Long userId,
                                     @RequestParam("reservationId") Long reservationId,
                                     @Valid @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {
        ReviewCreateResponseDto createResponseDto = reviewService.save(userId, reservationId, reviewCreateRequestDto);
        log.info("score={}", reviewCreateRequestDto.getScore());


        return new BaseResponse<>(createResponseDto);
    }

    @Operation(summary = "Getting All Reviews", description = "Review Read API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
    })
    @GetMapping("/reviews")
    public BaseResponse findAll(@RequestParam int page,
                                @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewListResponseDto> responseDtos = reviewService.findAll(pageable);
        return new BaseResponse(responseDtos);
    }

}

