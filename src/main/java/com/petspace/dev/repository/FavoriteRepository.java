package com.petspace.dev.repository;

import com.petspace.dev.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndRoomId(Long userId, Long roomId);

    List<Favorite> findAllByUserId(Long userid);
}
