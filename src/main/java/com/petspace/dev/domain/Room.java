package com.petspace.dev.domain;

import com.petspace.dev.domain.image.RoomImage;
import com.petspace.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    private List<Reservation> reservation;

    @OneToMany(mappedBy = "room")
    private List<RoomCategory> roomCategories = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<RoomAvailable> roomAvailables = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    List<Favorite> favorites = new ArrayList<>();

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
