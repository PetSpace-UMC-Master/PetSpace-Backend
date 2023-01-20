package com.petspace.dev.service;

import com.petspace.dev.domain.Room;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomDetailResponseDto getRoomDetail(Long roomId) {

        // TODO roomId Error Handling -> 'Exception 관련 commit by 상진' pull 이후에 진행.
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new IllegalArgumentException("RoomId 에 해당하는 Room Entity 없음"));

        return new RoomDetailResponseDto(room);
    }

}
