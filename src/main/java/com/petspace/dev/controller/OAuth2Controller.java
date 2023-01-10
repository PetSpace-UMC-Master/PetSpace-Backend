package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/users")
public class OAuth2Controller {

    private final UserService userService;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) throws BaseException {
        String access_Token = userService.getKaKaoAccessToken(code);
        userService.createKakaoUser(access_Token);
    }
}