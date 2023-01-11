package com.petspace.dev.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@DynamicInsert // 정확하게 알아보기 TODO
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private boolean privacyAgreement;
    private boolean marketingAgreement;
    private boolean hostPermission;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
//    @ColumnDefault("NONE")
//    처음 컬럼 디폴트값을 넣으면 테이블이 생성안됨 -> 따라서 컬럼 디폴트 쓰지말고 dto단에서 toEntity를 이용해 초기값을 세팅하자!!
    private OauthProvider oauthProvider;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
//    @ColumnDefault("ACTIVE")
    private Status status;

    //ROLE 추가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
