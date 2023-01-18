package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Favorite> favorites = new ArrayList<>();

    @Column(length = 45)
    private String username;

    @Column(length = 45)
    private String nickname;

    @Column(length = 45)
    private String birth;

    @Column(length = 45, nullable = false)
    private String email;

    private String password;

    private boolean privacyAgreement;
    private boolean marketingAgreement;
    private boolean hostPermission;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OauthProvider oauthProvider;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    @Builder
    public User(String username, String nickname, String birth, String email, String password,
                boolean privacyAgreement, boolean marketingAgreement, boolean hostPermission, OauthProvider oauthProvider, Status status) {
        this.username = username;
        this.nickname = nickname;
        this.birth = birth;
        this.email = email;
        this.password = password;
        this.privacyAgreement = privacyAgreement;
        this.marketingAgreement = marketingAgreement;
        this.hostPermission = hostPermission;
        this.oauthProvider = oauthProvider;
        this.status = status;
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
