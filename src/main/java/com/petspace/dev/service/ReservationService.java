package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Optional<Reservation> findById(Long itemId) {
        return reservationRepository.findById(itemId);
    }

    public List<Reservation> findRooms() {
        return reservationRepository.findAll();
    }



}
