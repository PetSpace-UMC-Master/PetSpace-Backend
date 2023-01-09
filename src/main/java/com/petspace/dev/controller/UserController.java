package com.petspace.dev.controller;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.UserSignUpReqDto;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Long signUpUser(@RequestBody UserSignUpReqDto dto){
        User newUserInfo = dto.toEntity();
        userService.signUp(newUserInfo);
        return newUserInfo.getId(); // 추후 화면 전환되도록 구성 ?
    }

}
