package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long makeReservation(Long userId, Long roomId, ReservationCreateRequestDto dto) {
        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException());
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException());
        //Reservation 생성
        Reservation reservation = Reservation.createReservation(user, room, dto);
        reservationRepository.save(reservation);
        return reservation.getId();
    }
}
