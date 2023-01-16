package com.petspace.dev.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private List<MultipartFile> reviewImages = new ArrayList<>();

    @NotEmpty(message = "score를 입력해주세요.")
    private Integer score;
    private String content;
}
