package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.exception.ReservationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationCreateResponseDto save(Long userId, Long roomId, ReservationCreateRequestDto dto) {
        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ReservationException(POST_RESERVATION_EMPTY_USER));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ReservationException(POST_RESERVATION_EMPTY_ROOM));

        //Reservation 생성
        Reservation reservation = Reservation.createReservation(user, room, dto);
        reservationRepository.save(reservation);

        return new ReservationCreateResponseDto(reservation);
    }

    public List<ReservationReadResponseDto> readUpComingReservation(Long userId) {

        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ReservationException(POST_RESERVATION_EMPTY_USER));
        List<Reservation> reservations = user.getReservations();

        return user.getReservations().stream()
                .filter(r -> r.getStartDate().toLocalDate().compareTo(LocalDate.now()) >= 0) //예약이 현재보다 나중에 있으면 ture
                .map(ReservationReadResponseDto :: new)
                .collect(Collectors.toList());
    }

    public List<ReservationReadResponseDto> readTerminateReservation(Long userId) {

        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ReservationException(POST_RESERVATION_EMPTY_USER));
        List<Reservation> reservations = user.getReservations();

        return user.getReservations().stream()
                .filter(r -> r.getStartDate().toLocalDate().compareTo(LocalDate.now()) < 0) //예약이 현재보다 나중에 있으면 ture
                .map(ReservationReadResponseDto :: new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long reservationId) {
        //엔티티 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 예약입니다."));
        Reservation.deleteReservation(reservation);
        return reservation.getId();
    }
}
