package com.petspace.dev.repository;

import com.petspace.dev.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findById(Long id);

    @Query("select rs from Reservation rs " +
            "join fetch rs.user " +
            "join fetch rs.room " +
            "where rs.id = :reservationId and rs.user.id = :userId")
    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);
}
