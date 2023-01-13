package com.petspace.dev.controller;

import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.petspace.dev.util.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public BaseResponse createReview(@RequestParam("userId") Long userId,
                                     @RequestParam("reservationId") Long reservationId,
                                     @RequestBody ReviewCreateRequestDto requestDto) {

        reviewService.save(userId, reservationId, requestDto);
        return new BaseResponse(SUCCESS);
    }
}

