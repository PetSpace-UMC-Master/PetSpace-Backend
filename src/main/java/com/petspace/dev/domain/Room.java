package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.image.RoomImage;
import com.petspace.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "room")
    private List<Review> reviews = new ArrayList<>();

    @JsonManagedReference //Json 순환참조 해결할 때 추가, 정확히 모르고 썼기 때문에 문제 발생시 다른 해결법 찾아야함
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservation = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<RoomCategory> roomCategories = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomFacility> roomFacilities = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomAvailable> roomAvailables = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    List<Favorite> favorites = new ArrayList<>();

    @JsonManagedReference //Json 순환참조 해결할 때 추가, 정확히 모르고 썼기 때문에 문제 발생시 다른 해결법 찾아야함
    @OneToMany(mappedBy = "room")
    List<RoomImage> roomImages = new ArrayList<>();

    @Embedded
    private Address address;

    @Column(length = 45, nullable = false)
    private String roomName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int maxGuest;

    @Column(nullable = false)
    private int maxPet;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    // TODO Default 처리 여부
    @Column(nullable = false)
    private LocalDateTime checkinTime;

    @Column(nullable = false)
    private LocalDateTime checkoutTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

}
