package com.petspace.dev.repository;

import com.petspace.dev.domain.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findById(Long id);

    @Query("SELECT r, count(rv.id) as review_count, avg(rv.score) as average_review_score From Room r " +
            "left outer join fetch Reservation rs on (r.id = rs.room.id)" +
            "left outer join fetch Review rv on (rs.id = rv.reservation.id) " +
            "WHERE r.status = 'ACTIVE'" +
            "group by r.id")
    List<Room> findAllDesc(Pageable pageable);

    @Query("SELECT r, count(rv.id) as review_count, avg(rv.score) as average_review_score From Room r " +
            "left outer join fetch Reservation rs on (r.id = rs.room.id) " +
            "left outer join fetch Review rv on (rs.id = rv.reservation.id) " +
            "left outer join fetch RoomCategory rc on (r.id = rc.room.id)" +
            "WHERE r.status = 'ACTIVE' and rc.category.id = :id " +
            "group by r.id")
    List<Room> findAllDescByCategory(Pageable pageable, @Param("id") Long categoryId);

    @Query("SELECT r From Room r where r.user.id = :id")
    List<Room> findAllDescByUserId(Pageable pageable, @Param("id") Long userId);

    @Query("SELECT r, count(rv.id) as review_count, avg(rv.score) as average_review_score From Room r " +
            "left outer join fetch r.reviews rv " +
            "where (SELECT count(ra.room.id) FROM r.roomAvailables ra where ra.availableDay >= :startDay and ra.availableDay < :endDay) = :days " +
            "and r.maxGuest >= :people and r.maxPet >= :pets " +
            "and (r.address.city LIKE %:keyword% or r.address.district LIKE %:keyword%) " +
            "group by r.id")
    List<Room> findAllDescByFilter(Pageable pageable, @Param("startDay") LocalDate startDay, @Param("endDay") LocalDate endDay,
                                   @Param("days") long days, @Param("people") Integer people, @Param("pets") Integer pets, @Param("keyword") String keyword);
}
