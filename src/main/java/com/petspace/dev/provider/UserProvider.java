package com.petspace.dev.provider;

import com.petspace.dev.Secret.Secret;
import com.petspace.dev.config.BaseException;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.PostLogInReq;
import com.petspace.dev.dto.PostLogInRes;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.utils.AES128;
import com.petspace.dev.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.petspace.dev.config.BaseResponseStatus.*;
import static com.petspace.dev.utils.ValidationRegex.isRegexEmail;

@Service
public class UserProvider {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    // 로그인(password 검사)
    public PostLogInRes logIn(PostLogInReq postLoginReq) throws BaseException {
        List<User> userList = userRepository.findByEmail(postLoginReq.getEmail()); // Req 내 email에 해당하는 User 정보 받아오기

        // 없는 Email
        if(userList.isEmpty()){
            throw new BaseException(FAILED_TO_LOGIN);
        }

        User user = userList.get(0);
        String password;

        // User 정보에서 패스워드 암호화하여 비교
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 암호화
            // 회원가입할 때 비밀번호가 암호화되어 저장되었기 떄문에 로그인을 할때도 암호화된 값끼리 비교를 해야합니다.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if (postLoginReq.getPassword().equals(password)) { // 비밀번호가 일치한다면 userIdx를 가져온다.
            // userIdx 는 암호화되기 전 패스워드를 기준으로 받아온다. 즉,
            Long userIdx = userRepository.findByEmail(postLoginReq.getEmail()).get(0).getId(); // User 에서 lombok 의 getter 에 의해 반환

            String jwt = jwtService.createJwt(userIdx); // userIdx 를 이용해서 JWT 생성
            return new PostLogInRes(userIdx, jwt);

        } else { // 비밀번호가 다르다면 에러메세지를 출력한다.
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }



}
