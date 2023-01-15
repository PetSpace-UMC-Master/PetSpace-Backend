package com.petspace.dev.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private List<MultipartFile> reviewImages = new ArrayList<>();
    private int score;
    private String content;
}
