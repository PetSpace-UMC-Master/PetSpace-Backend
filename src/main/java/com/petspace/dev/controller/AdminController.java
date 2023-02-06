package com.petspace.dev.controller;

import com.petspace.dev.dto.user.UserJoinRequestDto;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @RequestMapping("")
    public String home(){
        return "home";
    }

    /** 회원 가입 **/
    @GetMapping("/members/new")
    public String createMemberDto(Model model){
        model.addAttribute("userJoinRequestDto", new UserJoinRequestDto());
        return "members/createUserJoinRequestDto";
    }

    @PostMapping("/members/new")
    public String createMember(@Valid UserJoinRequestDto userJoinRequestDto, BindingResult result){
        userService.join(userJoinRequestDto);
        return "redirect:/admin";
    }

}
