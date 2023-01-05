package com.petspace.dev.controller;

import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.dto.UserRequestDto;
import com.petspace.dev.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/app/users")
//    @ApiOperation(value = "회원가입 API", notes = "회원가입form에서 정보를 받아 DB에 저장합니다")
    public ResponseDto signUp(@RequestBody UserRequestDto userRequestDto) {
        return userService.signup(userRequestDto);
    }


}