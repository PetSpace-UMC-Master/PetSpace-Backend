package com.petspace.dev.service;

import com.petspace.dev.dto.room.RoomListResponseDto;
import com.petspace.dev.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomRepository roomRepository;

    private static final int defaultPageSize = 5;

    @Transactional(readOnly = true)
    public List<RoomListResponseDto> findAllDesc(Optional<Integer> page, Optional<String> sortBy, Optional<String> order) {
        Sort sort = getSortBy(sortBy.orElse("id"), order.orElse("desc"));
        Pageable pageable = PageRequest.of(page.orElse(0), defaultPageSize, sort);
        return roomRepository.findAllDesc(pageable).stream()
                .map(RoomListResponseDto::new)
                .collect(Collectors.toList());
    }

    public Sort getSortBy(String column, String order) {
        if (order.equals("ASC")) {
            return Sort.by(column).ascending().and(Sort.by("id").descending());
        }
        return Sort.by(column).descending().and(Sort.by("id").descending());
    }
}
