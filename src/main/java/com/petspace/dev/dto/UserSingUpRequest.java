package com.petspace.dev.dto;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Role;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@RequiredArgsConstructor
public class UserSingUpRequest {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 주소를 입력해주세요")
    String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    String password;
    String username;
    String nickname;
    String birth;
    boolean marketingAgreement;
    boolean privacyAgreement;

    public UserSingUpRequest(String email, String password, String username, String nickname, String birth, boolean marketingAgreement, boolean privacyAgreement) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.birth = birth;
        this.marketingAgreement = marketingAgreement;
        this.privacyAgreement = privacyAgreement;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .birth(birth)
                .marketingAgreement(marketingAgreement)
                .privacyAgreement(true)
                .oauthProvider(OauthProvider.NONE)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();

    }
}
