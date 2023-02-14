package com.petspace.dev.repository;

import com.petspace.dev.domain.image.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
}
