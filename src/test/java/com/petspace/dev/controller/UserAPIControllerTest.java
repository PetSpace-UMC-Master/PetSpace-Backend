package com.petspace.dev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.user.UserRegisterRequestDto;
import com.petspace.dev.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAPIControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void user가_등록된다() throws Exception {
        //given
        String username = "username";
        String nickname = "nickname";
        String birth = "1997-06-27";
        String email = "woook27@naver.com";
        String password = "password";
        boolean marketingAgreement = false;
        OauthProvider oauthProvider = OauthProvider.NONE;
        Status status = Status.ACTIVE;

        UserRegisterRequestDto requestDto = UserRegisterRequestDto.builder().username(username).nickname(nickname).birth(birth).email(email).password(password)
                .marketingAgreement(marketingAgreement).build();


        String url = "http://localhost:" + port + "/user";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
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
