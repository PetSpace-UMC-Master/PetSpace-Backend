package com.petspace.dev.repository;

import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.image.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findById(Long id);
}
