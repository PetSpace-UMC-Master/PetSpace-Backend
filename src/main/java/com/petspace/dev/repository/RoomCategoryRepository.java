package com.petspace.dev.repository;

import com.petspace.dev.domain.RoomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomCategoryRepository extends JpaRepository<RoomCategory, Long> {
}
