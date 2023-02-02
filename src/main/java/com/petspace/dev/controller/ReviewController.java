package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.dto.review.ReviewDeleteResponseDto;
import com.petspace.dev.dto.review.ReviewListResponseDto;
import com.petspace.dev.dto.review.ReviewResponseDto;
import com.petspace.dev.dto.review.ReviewUpdateRequestDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Review Post", description = "Review Post API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewListResponseDto.class))),
            @ApiResponse(responseCode = "200", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "200", description = "해당 예약이 존재하지 않습니다."),
            @ApiResponse(responseCode = "200", description = "score를 입력해주세요.")
    })
    @PostMapping("/reviews")
    public BaseResponse<ReviewResponseDto> createReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @RequestParam("reservationId") Long reservationId,
                                     @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {

        Long userId = principalDetail.getId();
        ReviewResponseDto createResponseDto = reviewService.save(userId, reservationId, reviewCreateRequestDto);

        return new BaseResponse<>(createResponseDto);
    }

    @Operation(summary = "Getting All Reviews", description = "Review Read API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
    })
    @GetMapping("/reviews")
    public BaseResponse findAllReview(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReviewListResponseDto> responseDtos = reviewService.findAllReview(pageable);
        return new BaseResponse(responseDtos);
    }


    @Operation(summary = "Updating Review", description = "Review Update API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewListResponseDto.class))),
    })
    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @PathVariable Long reviewId,
                                     @ModelAttribute ReviewUpdateRequestDto reviewUpdateRequestDto) {
        Long userId = principalDetail.getId();
        ReviewResponseDto responseDto = reviewService.updateReview(userId, reviewId, reviewUpdateRequestDto);

        return new BaseResponse<>(responseDto);
    }


    @Operation(summary = "Updating Review", description = "Review Update API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewListResponseDto.class))),
    })
    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse deleteReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @PathVariable Long reviewId) {
        Long userId = principalDetail.getId();
        ReviewDeleteResponseDto responseDto = reviewService.deleteReview(userId, reviewId);

        return new BaseResponse(responseDto);
    }
}

