package com.petspace.dev.service;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.domain.HostPermission;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.ResponseDto;
import com.petspace.dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.petspace.dev.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;

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
    public Object signup(User user) throws BaseException {
        String password = passwordEncoder.encode(user.getPassword());

        // 비밀번호 암호화
        user.setPassword(password);
        user.setHostPermission(HostPermission.GUEST);
        user.setOauthProvider(OauthProvider.NONE);
        user.setPrivacyAgreement(user.isPrivacyAgreement());
        user.setStatus(Status.ACTIVE);

        // 이메일 중복 확인
        Optional<User> findByEmails = userRepository.findByEmail(user.getEmail());
        if (!findByEmails.isEmpty()) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        userRepository.save(user);
        return new ResponseDto("success", "회원가입에 성공하였습니다", "");
    }




}
