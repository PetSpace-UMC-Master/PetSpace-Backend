package com.petspace.dev.domain.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Room;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_image_id")
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String roomImageUrl;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    public RoomImage(Room room, String roomImageUrl) {
        this.room = room;
        this.roomImageUrl = roomImageUrl;
    }
}
