package com.petspace.dev.controller;

import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.dto.review.ReviewCreateResponseDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.petspace.dev.util.BaseResponseStatus.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public BaseResponse createReview(@RequestParam("userId") Long userId,
                                     @RequestParam("reservationId") Long reservationId,
                                     @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {
        ReviewCreateResponseDto createResponseDto = reviewService.save(userId, reservationId, reviewCreateRequestDto);
        log.info("score={}", reviewCreateRequestDto.getScore());


        return new BaseResponse<>(createResponseDto);
    }
}

