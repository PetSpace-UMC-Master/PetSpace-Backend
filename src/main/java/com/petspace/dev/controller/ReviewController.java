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
        System.out.println("controller content : " + reviewCreateRequestDto.getContent());
        System.out.println("controller score : " + reviewCreateRequestDto.getScore());
        System.out.println("controller userId : " + userId);
        System.out.println("controller reservationId : " + reservationId);

        return new BaseResponse<>(SUCCESS);
    }
}

