package com.petspace.dev.domain.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.BaseTimeEntity;
import com.petspace.dev.domain.Favorite;
import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String profileImage;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Favorite> favorites = new ArrayList<>();

    @Builder
    public User(String profileImage, String username, String nickname, String birth, String email, String password,
                boolean privacyAgreement, boolean marketingAgreement, boolean hostPermission,
                OauthProvider oauthProvider, Status status, Role role) {
        this.profileImage = profileImage;
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
        this.role = role;
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
}
