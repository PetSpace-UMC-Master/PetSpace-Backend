package com.petspace.dev.controller;

import com.petspace.dev.dto.review.*;
import com.petspace.dev.domain.user.auth.PrincipalDetails;
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
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewListResponseDto.class))),
            @ApiResponse(responseCode = "200", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "200", description = "해당 예약이 존재하지 않습니다."),
            @ApiResponse(responseCode = "200", description = "score를 입력해주세요.")
    })
    @PostMapping("/reviews")
    public BaseResponse createReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @RequestParam("reservationId") Long reservationId,
                                     @Valid @ModelAttribute ReviewCreateRequestDto reviewCreateRequestDto) {
        Long userId = principalDetail.getId();
        ReviewCreateResponseDto createResponseDto = reviewService.save(userId, reservationId, reviewCreateRequestDto);
        log.info("score={}", reviewCreateRequestDto.getScore());

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
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다.",
                    content = @Content(schema = @Schema(implementation = ReviewListResponseDto.class)))
    })
    @PatchMapping("/reviews/{idx}/update")
    public BaseResponse updateReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @PathVariable Long roomId,
                                     @Valid @ModelAttribute ReviewUpdateRequestDto reviewUpdateRequestDto) {
        Long userId = principalDetail.getId();
        ReviewUpdateResponseDto responseDto = reviewService.update(userId, roomId, reviewUpdateRequestDto);

        return new BaseResponse<>(responseDto);
    }

    @PatchMapping("/reviews/{roomId}/delete")
    public BaseResponse deleteReview(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                     @PathVariable Long roomId) {
        Long userId = principalDetail.getId();
        ReviewDeleteResponseDto responseDto = reviewService.delete(userId, roomId);

        return new BaseResponse(responseDto);
    }
}

