package com.petspace.dev.repository;

import com.petspace.dev.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndRoomId(Long userId, Long roomId);

    @Query("select f from Favorite f " +
            "join fetch f.user u " +
            "join fetch f.room r " +
            "where u.id = :userId and r.address.region = :region and f.isClicked = true")
    List<Favorite> findAllFavoritesByUserIdAndRegion(Long userId, String region);}