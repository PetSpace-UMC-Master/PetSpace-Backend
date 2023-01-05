package com.petspace.dev.repository;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void 유저_등록하기() {
        //given
        String username = "username";
        String nickname = "nickname";
        String birth = "1997-06-27";
        String email = "woook27@naver.com";
        String password = "password";
        boolean privacyAgreement = true;
        boolean marketingAgreement = false;
        boolean hostPermission = false;
        OauthProvider oauthProvider = OauthProvider.NONE;
        Status status = Status.ACTIVE;

        userRepository.save(User.builder().username(username).nickname(nickname).birth(birth).email(email).password(password)
                .privacyAgreement(privacyAgreement).marketingAgreement(marketingAgreement).hostPermission(hostPermission)
                .oauthProvider(oauthProvider).status(status).build());

        //when
        List<User> users = userRepository.findAll();

        //then
        User user = users.get(0);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getBirth()).isEqualTo(birth);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.isPrivacyAgreement()).isTrue();
        assertThat(user.isMarketingAgreement()).isFalse();
        assertThat(user.isHostPermission()).isFalse();
        assertThat(user.getOauthProvider()).isEqualTo(oauthProvider);
        assertThat(user.getStatus()).isEqualTo(status);
    }
}
