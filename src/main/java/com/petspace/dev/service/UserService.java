package com.petspace.dev.service;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponseStatus;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.PostSignUpReq;
import com.petspace.dev.dto.PostSignUpRes;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.petspace.dev.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     */
    @Transactional
    public PostSignUpRes signUp(User user) throws BaseException {
        validateDuplicateUser(user);
        userRepository.save(user);
        // TODO welcome 빼고, 어느 정보들 넘겨줄지 논의
        return new PostSignUpRes(user.getId(), user.getNickname()+" 님 반갑습니다.");
    }

    private void validateDuplicateUser(User user) throws BaseException {
        List<User> findUsers = userRepository.findByEmail(user.getEmail());
        if(!findUsers.isEmpty()){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
    }

    /**
     * 회원 조회
     */



    /**
     * 로그인 기능
     */

}
