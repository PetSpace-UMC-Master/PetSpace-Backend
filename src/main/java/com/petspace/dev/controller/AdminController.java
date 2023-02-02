package com.petspace.dev.controller;

import com.petspace.dev.dto.admin.RoomCreateRequestDto;
import com.petspace.dev.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @RequestMapping("/")
    public String home(){
        log.info("home controller");
        return "home";
    }

}
