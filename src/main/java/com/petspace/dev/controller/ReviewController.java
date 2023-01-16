package com.petspace.dev.controller;

import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
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
                                     @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {
        reviewService.save(userId, reservationId, reviewCreateRequestDto);

        return new BaseResponse<>(SUCCESS);
    }
}

