package com.petspace.dev.controller;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.UserJoinRequestDto;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public ResponseEntity<UserJoinRequestDto> join(@RequestBody UserJoinRequestDto dto) {
        User user = dto.toEntity();
        userService.join(user);
        return ResponseEntity.ok(dto);
    }
}
