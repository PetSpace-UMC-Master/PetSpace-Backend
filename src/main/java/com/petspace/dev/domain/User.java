package com.petspace.dev.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user")
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

    @Column(length = 2000)
    private String imgUrl;

    private boolean privacyAgreement;
    private boolean marketingAgreement;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private HostPermission hostPermission;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OauthProvider oauthProvider;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    @Builder
    public User(String nickname, String birth, String email, String imgUrl,
                OauthProvider oauthProvider, Status status, HostPermission hostPermission) {
        this.nickname = nickname;
        this.birth = birth;
        this.imgUrl = imgUrl;
        this.email = email;
        this.oauthProvider = oauthProvider;
        this.status = status;
        this.hostPermission = hostPermission;
    }

    public User update(String nickname, String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;

        return this;
    }

    public String getPermissionKey() {
        return this.hostPermission.getKey();
    }

}
