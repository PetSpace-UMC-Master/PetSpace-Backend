package com.petspace.dev.service;

import com.petspace.dev.domain.User;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long signUp(User user){
        validateDuplicateUser(user);
        userRepository.save(user);
        return user.getId();
    }

    // TODO 로그인 기능시 수정해서 사용 가능할 듯
    private void validateDuplicateUser(User user){
        List<User> findUsers = userRepository.findByEmail(user.getEmail());

        if(!findUsers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 조회
     */



    /**
     * 로그인 기능
     */

}
