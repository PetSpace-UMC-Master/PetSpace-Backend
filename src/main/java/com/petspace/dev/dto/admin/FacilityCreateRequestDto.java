package com.petspace.dev.dto.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class FacilityCreateRequestDto {

    @NotBlank(message = "facilityName 을 입력해주세요.")
    private String facilityName;

    private MultipartFile facilityImage;

    @NotBlank(message = "category 을 입력해주세요.")
    private String category;

}
