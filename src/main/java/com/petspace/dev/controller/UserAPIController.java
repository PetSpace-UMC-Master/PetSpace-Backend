package com.petspace.dev.controller;

import com.petspace.dev.dto.user.UserRegisterRequestDto;
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
    public Long register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return userService.register(userRegisterRequestDto);
    }
}
