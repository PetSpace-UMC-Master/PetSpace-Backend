package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.domain.HostPermission;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.SessionUserDto;
import com.petspace.dev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @PostMapping("/app/signup")
//    @ApiOperation(value = "회원가입 API", notes = "회원가입form에서 정보를 받아 DB에 저장합니다")
    public Object signUp(@RequestBody SessionUserDto userDto) throws BaseException {

        if (!isRegexEmail(userDto.getEmail())) {
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        if (userDto.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        if (userDto.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
        }
        String username = userDto.getUsername();
        String nickname = userDto.getNickname();
        String birth = userDto.getBirth();
        String email = userDto.getEmail();
        String imgUrl = userDto.getImgUrl();
        String password = userDto.getPassword();
        OauthProvider oauthProvider = OauthProvider.NONE;
        Status status = Status.ACTIVE;
        HostPermission hostPermission = HostPermission.GUEST;

        User user = new User(username, nickname, birth, email, password, imgUrl, oauthProvider, status, hostPermission);



        try {
            return userService.signup(user);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
