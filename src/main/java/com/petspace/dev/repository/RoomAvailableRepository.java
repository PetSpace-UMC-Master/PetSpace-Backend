package com.petspace.dev.repository;

import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.RoomAvailable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomAvailableRepository extends JpaRepository<RoomAvailable, Long> {

    List<RoomAvailable> findAllByRoom(Room room);
}
