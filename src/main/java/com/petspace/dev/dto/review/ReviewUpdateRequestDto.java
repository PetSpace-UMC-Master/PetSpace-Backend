package com.petspace.dev.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewUpdateRequestDto {
    private List<MultipartFile> reviewImages = new ArrayList<>();
    private Integer score;
    private String content;
}
