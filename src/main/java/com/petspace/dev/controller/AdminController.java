package com.petspace.dev.controller;

import com.petspace.dev.dto.user.UserJoinRequestDto;

import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.service.AdminService;
import java.util.List;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @RequestMapping("")
    public String home(){
        return "home";
    }

    /** 회원 가입 **/
    @GetMapping("/users/new")
    public String createUserDto(Model model){
        model.addAttribute("userJoinRequestDto", new UserJoinRequestDto());
        return "users/createUserJoinRequestDto";
    }

    @PostMapping("/users/new")
    public String createUser(@Valid UserJoinRequestDto userJoinRequestDto, BindingResult result){
        userService.join(userJoinRequestDto);
        return "redirect:/admin";
    }


    /** 회원 조회 **/
    @GetMapping("/users")
    public String list(Model model){
        List<User> users = adminService.findUsers();
        model.addAttribute("users", users);
        return "users/userList";
    }

}
