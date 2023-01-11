package com.petspace.dev.controller;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.LoginResponseDto;
import com.petspace.dev.dto.OauthUserDto;
import com.petspace.dev.dto.SessionUserDto;
import com.petspace.dev.security.jwt.JwtTokenProvider;
import com.petspace.dev.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/app/users")
public class OAuth2Controller {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) throws BaseException {
        String access_Token = code;
        userService.createKakaoUser(access_Token);

//        String jwt_token = jwtTokenProvider.createToken(userDto.getEmail());
//        return new LoginResponseDto("success", "로그인 성공했습니다", "");

    }
}