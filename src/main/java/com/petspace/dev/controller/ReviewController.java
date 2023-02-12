package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.review.ReviewDeleteResponseDto;
import com.petspace.dev.dto.review.ReviewRequestDto;
import com.petspace.dev.dto.review.ReviewResponseDto;
import com.petspace.dev.dto.review.ReviewsResponseDto;
import com.petspace.dev.dto.review.ReviewsSliceResponseDto;
import com.petspace.dev.service.ReviewService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/reviews")
    public BaseResponse<ReviewResponseDto> createReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                          @RequestParam("reservationId") Long reservationId,
                                                          @RequestParam(value = "reviewImages", required = false) List<MultipartFile> reviewImages,
                                                          @RequestParam(value = "score", required = false, defaultValue = "0") int score,
                                                          @RequestParam(value = "content", required = false) String content) {

        Long userId = principalDetail.getId();
        ReviewRequestDto requestDto = ReviewRequestDto.of(reviewImages, score, content);
        ReviewResponseDto createResponseDto = reviewService.save(userId, reservationId, requestDto);
        return new BaseResponse<>(createResponseDto);
    }


    @Operation(summary = "Getting All Reviews", description = "Review Read API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
    })
    @GetMapping("/reviews/{roomId}")
    public BaseResponse getAllReviews(@PathVariable Long roomId,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        ReviewsSliceResponseDto responseDto = reviewService.findAllReviewsByPage(roomId, pageRequest);
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Updating Review", description = "Review Update API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewsResponseDto.class))),
    })
    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<ReviewResponseDto> updateReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @PathVariable Long reviewId,
                                     @ModelAttribute ReviewRequestDto reviewUpdateRequestDto) {
        Long userId = principalDetail.getId();
        ReviewResponseDto responseDto = reviewService.updateReview(userId, reviewId, reviewUpdateRequestDto);

        return new BaseResponse<>(responseDto);
    }


    @Operation(summary = "Updating Review", description = "Review Update API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewsResponseDto.class))),
    })
    @DeleteMapping("/reviews/{reviewId}")
    public BaseResponse<ReviewDeleteResponseDto> deleteReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                              @PathVariable Long reviewId) {
        Long userId = principalDetail.getId();
        ReviewDeleteResponseDto deleteResponseDto = reviewService.deleteReview(userId, reviewId);

        return new BaseResponse<>(deleteResponseDto);
    }
}

