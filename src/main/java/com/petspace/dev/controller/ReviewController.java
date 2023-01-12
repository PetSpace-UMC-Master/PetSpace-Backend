package com.petspace.dev.controller;

import com.petspace.dev.domain.Review;
import com.petspace.dev.dto.review.ReviewRequestDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.petspace.dev.util.BaseResponseStatus.SUCCESS;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review")
    public BaseResponse createReview(@RequestParam("userId") Long userId,
                                     @RequestParam("reservationId") Long reservationId,
                                     @RequestBody ReviewRequestDto requestDto) {

        reviewService.save(userId, reservationId, requestDto);
        return new BaseResponse(SUCCESS);
    }
}
//    @PostMapping("/api/login")
//    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto dto) {
//        User loginUser = dto.toEntity();
//        UserLoginResponseDto loginResponseDto = userService.login(loginUser);
//        return ResponseEntity.ok(loginResponseDto);
//    }
//}
