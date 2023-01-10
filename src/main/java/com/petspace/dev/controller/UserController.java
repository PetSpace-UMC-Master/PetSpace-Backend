package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponse;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.LoginResponseDto;
import com.petspace.dev.security.jwt.JwtTokenProvider;
import com.petspace.dev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static com.petspace.dev.config.BaseResponseStatus.*;
import static com.petspace.dev.utils.ValidationRegex.isRegexEmail;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired // 빈 주입
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //회원가입
    @PostMapping("/app/signup")
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

        try {
            return userService.signup(user);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    @PostMapping("/app/signin")
    public LoginResponseDto login(@RequestBody HashMap<String, String> map, HttpServletResponse response) throws BaseException {
        String email = map.get("email");
        String password = map.get("password");
        System.out.println(email);
        System.out.println(password);
        User user = userService.login(email, password);
        String checkEmail = user.getEmail();

        String token = jwtTokenProvider.createToken(checkEmail);
        response.setHeader("X-AUTH-TOKEN", token);

        //header에 cookie 저장
        Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        //body에도 보내주기
        return new LoginResponseDto("success", "로그인 성공했습니다", token, user.getId());
    }

}
