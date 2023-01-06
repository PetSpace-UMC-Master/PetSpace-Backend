package com.petspace.dev.controller;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.dto.UserRequestDto;
import com.petspace.dev.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.petspace.dev.utils.ValidationRegex.isRegexEmail;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/app/signin")
//    @ApiOperation(value = "회원가입 API", notes = "회원가입form에서 정보를 받아 DB에 저장합니다")
    public ResponseDto signUp(@RequestBody User user) {

        if(!isRegexEmail(user.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        return userService.signup(user);
    }


}
