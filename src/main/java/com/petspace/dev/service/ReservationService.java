package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation makeReservation(Long userId, Long roomId, ReservationCreateRequestDto dto) {
        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("해당 숙속가 존재하지 않습니다."));
        //Reservation 생성
        Reservation reservation = Reservation.createReservation(user, room, dto);
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<ReservationReadResponseDto> readReservation(Long userId) {

        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("해당 유저가 존재하지 않습니다. id=" + userId));
        List<Reservation> reservations = user.getReservations();

        List<ReservationReadResponseDto> reservationReadResponseDtoList = new ArrayList<>();
        for(Reservation reservation : reservations) {
            ReservationReadResponseDto dto = ReservationReadResponseDto.builder()
                    .reservationCode(reservation.getReservationCode())
                    .roomName(reservation.getRoom().getRoomName())
                    .roomImages(reservation.getRoom().getRoomImages())
                    .startDate(reservation.getStartDate().toLocalDate())
                    .endDate(reservation.getEndDate().toLocalDate())
                    .remainingDays(0)
                    .build();
            dto.setRemainingDays();
            reservationReadResponseDtoList.add(dto);
        }
        return reservationReadResponseDtoList;
    }
}
