package com.petspace.dev.service;

import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long register(UserRegisterRequestDto userRegisterRequestDto) {
        return userRepository.save(userRegisterRequestDto.toEntity()).getId();
    }
}
