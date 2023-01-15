package com.petspace.dev.controller;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserJoinRequestDto;
import com.petspace.dev.dto.user.UserLoginRequestDto;
import com.petspace.dev.dto.user.UserLoginResponseDto;
import com.petspace.dev.service.UserService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public BaseResponse<UserJoinRequestDto> join(@Valid @RequestBody UserJoinRequestDto dto) {
        User user = dto.toEntity();
        userService.join(user);
        return new BaseResponse<>(dto);
    }

    // 중복 확인
    @GetMapping("/api/users/email-check")
    public BaseResponse<Object> checkEmail(@RequestParam String email) {
        return userService.checkEmailDuplicate(email);
    }


    @PostMapping("/api/login")
    public BaseResponse<UserLoginResponseDto> login(@RequestBody UserLoginRequestDto dto) {
        User loginUser = dto.toEntity();
        UserLoginResponseDto loginResponseDto = userService.login(loginUser);
        return new BaseResponse<>(loginResponseDto);
    }
}
