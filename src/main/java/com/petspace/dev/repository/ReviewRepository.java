package com.petspace.dev.repository;

import com.petspace.dev.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.status='ACTIVE' ORDER BY r.id DESC ")
    List<Review> findAllDesc(Pageable pageable);

    @Query("select r from Review r " +
            "join fetch r.reservation rs " +
            "where r.id = :reviewId and rs.user.id = :userId")
    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);
}
