package com.petspace.dev.repository;

import com.petspace.dev.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r " +
            "join fetch r.reservation rs " +
            "where r.status = 'ACTIVE' and r.id = :reviewId and rs.user.id = :userId")
    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);

    @Query("select r from Review r " +
            "join fetch r.reservation rs " +
            "where r.status = 'ACTIVE' and rs.room.id = :roomId")
    List<Review> findAllRoomId(Long roomId);
}
