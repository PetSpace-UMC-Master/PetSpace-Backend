package com.petspace.dev.repository;

import com.petspace.dev.domain.User;

import com.petspace.dev.dto.SessionUserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
