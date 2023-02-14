package com.petspace.dev.dto.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CategoryCreateRequestDto {

    @NotBlank(message = "categoryName 을 입력해주세요.")
    private String categoryName;

    private MultipartFile categoryImage;

}
