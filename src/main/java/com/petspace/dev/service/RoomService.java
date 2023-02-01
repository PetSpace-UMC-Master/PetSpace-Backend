package com.petspace.dev.service;

import com.petspace.dev.domain.Favorite;
import com.petspace.dev.domain.Room;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.dto.room.RoomFacilityResponseDto;
import com.petspace.dev.repository.FavoriteRepository;
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
    private final FavoriteRepository favoriteRepository;

    @Transactional(readOnly = true)
    public RoomDetailResponseDto getRoomDetail(Long roomId, Long userId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new RoomException(NONE_ROOM));

        RoomDetailResponseDto roomDetailResponseDto = new RoomDetailResponseDto(room);

        // 로그인 상태
        if(userId != null){
            Favorite favorite = favoriteRepository.findByUserIdAndRoomId(userId, roomId)
                    .orElse(null);

            if(favorite != null && favorite.isClicked() == true){
                roomDetailResponseDto.setFavorite(true);
            } else{
                roomDetailResponseDto.setFavorite(false);
            }
        } else{
            roomDetailResponseDto.setFavorite(false);
        }

        return roomDetailResponseDto;
    }

    @Transactional(readOnly = true)
    public RoomFacilityResponseDto getRoomFacilities(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new RoomException(NONE_ROOM));

        return new RoomFacilityResponseDto(room);
    }
}
