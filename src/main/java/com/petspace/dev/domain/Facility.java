package com.petspace.dev.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Long id;

    @OneToMany(mappedBy = "facility")
    private List<RoomFacility> roomFacilities;

    @Column(length = 45, nullable = false)
    private String facilityName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String facilityImageUrl;

    @Column(nullable = false)
    private String category;

    @Builder
    public Facility(String facilityName, String facilityImageUrl, String category) {
        this.facilityName = facilityName;
        this.facilityImageUrl = facilityImageUrl;
        this.category = category;
    }

}
