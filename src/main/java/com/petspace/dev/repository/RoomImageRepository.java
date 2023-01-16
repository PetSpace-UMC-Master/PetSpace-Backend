package com.petspace.dev.repository;

import com.petspace.dev.domain.image.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {

    List<RoomImage> findByRoomId(Long id);

}
