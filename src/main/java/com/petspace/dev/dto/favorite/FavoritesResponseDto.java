package com.petspace.dev.dto.favorite;

import com.petspace.dev.domain.Favorite;
import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.RoomAvailable;
import com.petspace.dev.domain.image.RoomImage;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.petspace.dev.domain.Status.ACTIVE;

@Getter
@Builder
@Slf4j
public class FavoritesResponseDto {

    private Long id;
    private List<String> roomImages;
    private String roomAddress;
    private int price;
    private double averageReviewScore;
    private int numberOfReview;
    private List<LocalDate> availableDays;

    public static FavoritesResponseDto of(Favorite favorite) {

        // TODO Favorite을 Room으로 묶어서 리팩토링을 진행해야되나? 하나의 DTO / Service가 너무 많은 역할
        Room room = favorite.getRoom();

        List<Review> reviews = room.getReservation().stream()
                .map(Reservation::getReview)
                .filter(Objects::nonNull)
                .filter(review -> review.getStatus().equals(ACTIVE))
                .collect(Collectors.toList());

        double averageReviewScores = reviews.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0);

        List<LocalDate> availableDays = room.getRoomAvailables().stream()
                .filter(roomAvailable -> roomAvailable.getStatus().equals(ACTIVE))
                .map(RoomAvailable::getAvailableDay)
                .collect(Collectors.toList());

        return FavoritesResponseDto.builder()
                .id(room.getId())
                .roomImages(room.getRoomImages().stream().map(RoomImage::getRoomImageUrl).collect(Collectors.toList()))
                .roomAddress(room.getAddress().getDistrict() + ", " + room.getAddress().getCity())
                .price(room.getPrice())
                .averageReviewScore(averageReviewScores)
                .numberOfReview(reviews.size())
                .availableDays(availableDays)
                .build();
    }

}
