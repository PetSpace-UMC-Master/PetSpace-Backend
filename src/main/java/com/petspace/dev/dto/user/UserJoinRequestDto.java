package com.petspace.dev.dto.user;

import com.petspace.dev.domain.OauthProvider;
import com.petspace.dev.domain.Role;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class UserJoinRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "\\w+@\\w+\\.\\w+(\\.\\w+)?", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[~!@#$%^&*()_+])[A-Za-z0-9~!@#$%^&*()_+]{8,}", message = "특수문자 포함 8자 이상 입력해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인값을 입력해주세요.")
    private String checkedPassword;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣]{2,8}", message = "이름은 2-8자의 한글로 입력해주세요.")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 16, message = "닉네임은 2-16자로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "생일을 입력해주세요.")
    @Pattern(regexp = "^(19[0-9][0-9]|20[0-9]{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "생일 형식이 올바르지 않습니다.")
    private String birth;

    private boolean marketingAgreement;

    // dto -> entity
    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .username(username)
                .nickname(nickname)
                .birth(birth)
                .marketingAgreement(marketingAgreement)
                .privacyAgreement(true)
                .hostPermission(false)
                .oauthProvider(OauthProvider.NONE)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();
    }
}
