package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.dto.user.UserRegisterResponseDto;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.petspace.dev.common.Validation.validateUserRegisterRequestDto;

@RequiredArgsConstructor
@RestController
public class UserAPIController {

    private final UserService userService;
    private BindingResult bindingResult;

    @PostMapping("/user")
    public BaseResponse<UserRegisterResponseDto> register(@Validated @RequestBody UserRegisterRequestDto userRegisterRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new BaseResponse<>((
                    validateUserRegisterRequestDto(bindingResult.getFieldError()).getStatus()));
        }

        UserRegisterResponseDto userRegisterResponseDto = null;
        try {
            userRegisterResponseDto = userService.register(userRegisterRequestDto);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
        return new BaseResponse<>(userRegisterResponseDto);
    }
}
