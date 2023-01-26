package com.petspace.dev.service;

import com.petspace.dev.domain.Room;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.util.exception.RoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public RoomDetailResponseDto getRoomDetail(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new RoomException(NONE_ROOM));

        return new RoomDetailResponseDto(room);
    }

}
