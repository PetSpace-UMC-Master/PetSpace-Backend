package com.petspace.dev.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @Column(length = 45, nullable = false)
    private String facilityName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String facilityImageUrl;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "facility")
    private List<RoomFacility> roomFacilities;

    @Builder
    public Facility(String facilityName, String facilityImageUrl, String category) {
        this.facilityName = facilityName;
        this.facilityImageUrl = facilityImageUrl;
        this.category = category;
    }
}
