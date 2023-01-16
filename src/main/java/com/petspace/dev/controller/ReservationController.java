package com.petspace.dev.controller;

import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservations/{userId}/{roomId}")
    public ResponseEntity<ReservationCreateRequestDto> make(@PathVariable Long userId, @PathVariable Long roomId, @RequestBody ReservationCreateRequestDto dto) {
        reservationService.makeReservation(userId, roomId, dto);
        return ResponseEntity.ok(dto);
    }
}
