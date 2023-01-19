package com.petspace.dev.service;

import com.petspace.dev.util.input.room.CategoryType;
import com.petspace.dev.util.input.room.SortBy;
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

    private static final int DEFAULT_PAGE_SIZE = 5;

    @Transactional(readOnly = true)
    public List<RoomListResponseDto> findAllDesc(Optional<Integer> page,
                                                 Optional<SortBy> sortBy) {
        Sort sort = getSortBy(sortBy.orElse(SortBy.ID_DESC));
        Pageable pageable = PageRequest.of(page.orElse(0), DEFAULT_PAGE_SIZE, sort);

        return roomRepository.findAllDesc(pageable).stream()
                .map(RoomListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomListResponseDto> findAllDescByCategory(Optional<Integer> page,
                                                 Optional<SortBy> sortBy,
                                                 CategoryType categoryType) {
        Sort sort = getSortBy(sortBy.orElse(SortBy.ID_DESC));
        Pageable pageable = PageRequest.of(page.orElse(0), DEFAULT_PAGE_SIZE, sort);

        return roomRepository.findAllDescByCategory(pageable, categoryType.getCategoryId()).stream()
                .map(RoomListResponseDto::new)
                .collect(Collectors.toList());
    }

    public Sort getSortBy(SortBy sortBy) {
        if (sortBy.getOrderType().equals("ASC")) {
            return Sort.by(sortBy.getSortType()).ascending().and(Sort.by("id").descending());
        }
        return Sort.by(sortBy.getSortType()).descending().and(Sort.by("id").descending());
    }
}
