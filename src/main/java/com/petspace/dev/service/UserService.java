package com.petspace.dev.service;

import com.petspace.dev.domain.User;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }
}
