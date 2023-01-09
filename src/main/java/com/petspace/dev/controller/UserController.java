package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.PostLogInReq;
import com.petspace.dev.dto.PostLogInRes;
import com.petspace.dev.dto.PostSignUpReq;
import com.petspace.dev.dto.PostSignUpRes;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.petspace.dev.config.BaseResponseStatus.*;
import static com.petspace.dev.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public BaseResponse<PostSignUpRes> signUpUser(@RequestBody PostSignUpReq postSignUpReq){


        /**
         * 회원 가입 (Service 에서 내부적으로 기존회원 체크 후 Exception 처리)
         */
        User newUserInfo = postSignUpReq.toEntity();
        try{
            PostSignUpRes postSignUpRes = userService.signUp(newUserInfo);
            // 기존 회원 없어, 정상 로그인
            return new BaseResponse<>(postSignUpRes);
        }catch (BaseException e){
            // 기존 회원 있어, 비정상 종료
            return new BaseResponse<>(e.getStatus());
        }
    }

//    @PostMapping("/log-in")
//    public BaseResponse<PostLogInRes> logIn(@RequestBody PostLogInReq postLoginReq){
//
//    }

}
