package com.petspace.dev.service;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponseStatus;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.dto.user.UserRegisterResponseDto;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) throws BaseException {
        if(isEmailExist(userRegisterRequestDto.getEmail())) {
            throw new BaseException(BaseResponseStatus.POST_USER_EXISTS_EMAIL);
        }
        User user = userRepository.save(userRegisterRequestDto.toEntity());
        return new UserRegisterResponseDto(user.getId());
    }

    /**
     * 이메일 중복 여부를 확인
     *
     * @param email
     * @return true | false
     */
    private boolean isEmailExist(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        return !byEmail.isEmpty();
    }
}
