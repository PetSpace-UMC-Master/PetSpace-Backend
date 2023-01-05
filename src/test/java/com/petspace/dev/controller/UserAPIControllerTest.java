package com.petspace.dev.controller;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPIControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void user가_등록된다() throws Exception {
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

        UserRegisterRequestDto requestDto = UserRegisterRequestDto.builder().username(username).nickname(nickname).birth(birth).email(email).password(password)
                .marketingAgreement(marketingAgreement).build();


        String url = "http://localhost:" + port + "/user";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<User> all = userRepository.findAll();
        User user = all.get(0);
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
