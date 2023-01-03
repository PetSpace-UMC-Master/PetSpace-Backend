package com.petspace.dev.domain;

import com.petspace.backend.domain.image.RoomImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    private String roomName;

    private int price;
    private int maxGuest;
    private int maxPet;

    private String description;

    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;

    private String status;
}
