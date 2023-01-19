package com.petspace.dev.repository;

import com.petspace.dev.domain.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r, count(r1.id) as review_count, avg(r2.score) as avg_review_score From Room r " +
            "left outer join fetch Reservation r1 on (r.id = r1.room.id)" +
            "left outer join fetch Review r2 on (r1.id = r2.reservation.id) " +
            "WHERE r.status = 'ACTIVE'" +
            "group by r.id")
    List<Room> findAllDesc(Pageable pageable);

    @Query("SELECT r, count(r1.id) as review_count, avg(r2.score) as avg_review_score From Room r " +
            "left outer join fetch Reservation r1 on (r.id = r1.room.id) " +
            "left outer join fetch Review r2 on (r1.id = r2.reservation.id) " +
            "left outer join fetch RoomCategory rc on (r.id = rc.room.id)" +
            "WHERE r.status = 'ACTIVE' and rc.category.id = :id " +
            "group by r.id")
    List<Room> findAllDescByCategory(Pageable pageable, @Param("id") Long categoryId);
}