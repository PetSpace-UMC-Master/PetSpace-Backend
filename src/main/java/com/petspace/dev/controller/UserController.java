package com.petspace.dev.controller;

import com.petspace.dev.Response;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Role;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.UserSingUpRequest;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.security.JwtTokenProvider;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    @PostMapping (value = "/join")
    public Long singUp(@RequestBody @Valid UserSingUpRequest userSingUpRequest) {
        userSingUpRequest.setPassword(passwordEncoder.encode(userSingUpRequest.getPassword()));
        User user = userSingUpRequest.toEntity();
        Long userId = userService.singIn(user);

        return  userId;
    }

    //로그인
    @PostMapping(value = "/login")
    public String login(@RequestBody Map<String, String> user) {

        return userService.loginJwt(user);
    }

    //kakao
    @ResponseBody
    @GetMapping(value = "/kakao")
    public void kakaoCallBack(@RequestParam String code) {
        System.out.println(code);
    }
}
