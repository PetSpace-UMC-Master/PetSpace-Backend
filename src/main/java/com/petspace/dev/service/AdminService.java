package com.petspace.dev.service;

import com.petspace.dev.domain.user.User;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public List<User> findUsers() {
        log.info("@@ findUsers");
        return userRepository.findAll();
    }

    public User findUserById(Long userId){
        log.info("@@ findUserById");
        // 있는 유저 기준으로 출력하고, 해당 유저 id 로 받아오는 것이므로, null 없다 가정
        return userRepository.findById(userId).get();
    }
}
