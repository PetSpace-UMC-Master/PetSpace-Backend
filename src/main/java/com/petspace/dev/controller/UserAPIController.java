package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.dto.user.UserRegisterResponseDto;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserAPIController {

    private final UserService userService;

    @PostMapping("/user")
    public BaseResponse<UserRegisterResponseDto> register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        UserRegisterResponseDto userRegisterResponseDto = null;
        try {
            userRegisterResponseDto = userService.register(userRegisterRequestDto);
        } catch (BaseException e) {
            return new BaseResponse<>((e.getStatus()));
        }
        return new BaseResponse<>(userRegisterResponseDto);
    }
}
