package com.petspace.dev.repository;

import com.petspace.dev.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r " +
            "join fetch r.reservation rs " +
            "where r.status = 'ACTIVE' and r.id = :reviewId and rs.user.id = :userId")
    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);

    @Query("select r from Review r " +
            "left join fetch r.reservation " +
            "left join fetch r.reviewImages " +
            "where r.status = 'ACTIVE' and r.reservation.room.id = :roomId " +
            "order by r.id desc")
    Slice<Review> findAllReviewsSliceBy(Long roomId, Pageable pageable);
}
