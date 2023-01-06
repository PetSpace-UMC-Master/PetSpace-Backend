package com.petspace.dev.service;

import com.petspace.dev.domain.User;
import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.dto.UserRequestDto;
import com.petspace.dev.exception.CustomErrorException;
import com.petspace.dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
@Transactional(readOnly = true)
public class UserService {

    private Environment env;
//    private final Environment env;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private String ADMIN_TOKEN = env.getProperty("admin.ADMIN_TOKEN");

    @Autowired
    public UserService(Environment env, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.env = env;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseDto signup(User user){
//        String username = userRequestDto.getUsername();
////        String nickname = userRequestDto.getNickname();
////        String birth = userRequestDto.getBirth();
////        String email = userRequestDto.getEmail();
////        String password = passwordEncoder.encode(userRequestDto.getPassword());
////        boolean privacyAgreement = userRequestDto.isPrivacyAgreement();
////        boolean marketingAggrement = userRequestDto.isMarketingAgreement();
////        boolean hostPermission = userRequestDto.isHostPermission();
////        String oauthProvider = userRequestDto.getOauthProvider();
////        String status = userRequestDto.getStatus();
///
//        User user = new User(username, nickname, birth,email, password, privacyAgreement, marketingAggrement, hostPermission,oauthProvider,status)
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
