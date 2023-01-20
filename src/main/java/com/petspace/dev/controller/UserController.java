package com.petspace.dev.controller;

import com.petspace.dev.dto.user.UserCheckEmailResponseDto;
import com.petspace.dev.dto.user.UserJoinRequestDto;
import com.petspace.dev.dto.user.UserLoginRequestDto;
import com.petspace.dev.dto.user.UserLoginResponseDto;
import com.petspace.dev.dto.user.UserResponseDto;
import com.petspace.dev.service.UserService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public BaseResponse<UserResponseDto> join(@Valid @RequestBody UserJoinRequestDto joinRequestDto) {
        UserResponseDto responseDto = userService.join(joinRequestDto);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/sign-up/email-check")
    public BaseResponse<UserCheckEmailResponseDto> checkEmail(@RequestParam String email) {
        UserCheckEmailResponseDto checkEmailResponseDto = userService.checkEmailDuplicate(email);
        return new BaseResponse<>(checkEmailResponseDto);
    }

    @PostMapping("/login")
    public BaseResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto loginRequestDto) {
        UserLoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return new BaseResponse<>(loginResponseDto);
    }
}
