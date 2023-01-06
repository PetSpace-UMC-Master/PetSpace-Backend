package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.domain.User;
import com.petspace.dev.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.petspace.dev.config.BaseResponseStatus.*;
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
    public Object signUp(@RequestBody User user) throws BaseException {

        if (!isRegexEmail(user.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if (user.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        if (user.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }

        try{
            return userService.signup(user);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
