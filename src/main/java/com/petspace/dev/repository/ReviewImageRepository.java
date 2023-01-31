package com.petspace.dev.repository;

import com.petspace.dev.domain.image.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    @Modifying
    @Query("delete from ReviewImage rv " +
            "where rv.review.id = :reviewId")
    void deleteAllByIdInBatch(Long reviewId);
}
