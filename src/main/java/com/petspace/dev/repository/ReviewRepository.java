package com.petspace.dev.repository;

import com.petspace.dev.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r ORDER BY r.id DESC ")
    List<Review> findAllDesc();
}
