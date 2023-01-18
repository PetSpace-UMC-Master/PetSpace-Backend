package com.petspace.dev.repository;

import com.petspace.dev.domain.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r From Room r WHERE r.status = 'ACTIVE' order by r.id desc")
    List<Room> findAllDesc(Pageable pageable);
}