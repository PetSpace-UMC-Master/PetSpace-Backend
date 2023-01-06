package com.petspace.dev.service;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.exception.CustomErrorException;
import com.petspace.dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseDto signup(User user){
        String password = passwordEncoder.encode(user.getPassword());

        // 비밀번호 암호화
        user.setPassword(password);

        validateDuplicateUser(user);
        userRepository.save(user);
        return new ResponseDto("success", "회원가입에 성공하였습니다", "");
    }

    private void validateDuplicateUser(User user) {
        List<User> findByEmails = userRepository.findByEmail(user.getEmail());
        if (!findByEmails.isEmpty()) {
            throw new CustomErrorException("회원가입에 실패하였습니다.");
        }
    }

}
