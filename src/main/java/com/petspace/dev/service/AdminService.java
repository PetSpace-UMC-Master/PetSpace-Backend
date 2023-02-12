package com.petspace.dev.service;

import com.petspace.dev.domain.Facility;
import com.petspace.dev.domain.RoomFacility;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.dto.admin.FacilityCreateRequestDto;
import com.petspace.dev.repository.FacilityRepository;
import com.petspace.dev.repository.RoomFacilityRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final RoomFacilityRepository roomFacilityRepository;
    private final AwsS3Uploader awsS3Uploader;

    public List<User> findUsers() {
        log.info("@@ findUsers");
        return userRepository.findAll();
    }

    public User findUserById(Long userId){
        log.info("@@ findUserById");
        // 있는 유저 기준으로 출력하고, 해당 유저 id 로 받아오는 것이므로, null 없다 가정
        return userRepository.findById(userId).get();
    }

    /** 편의시설 */
    public List<Facility> findAllFacilities(){
        log.info("@@ findAllFacilities");
        return facilityRepository.findAll();
    }

    public String addFacility(FacilityCreateRequestDto facilityCreateRequestDto) {

        // 이미 존재하는 카테고리라면 추가 X // TODO nonNull 적절한 위치인지 체크 필요
        List<String> currentCategories = findAllFacilities().stream()
                .filter(Objects::nonNull)
                .map(Facility::getCategory)
                .distinct()
                .collect(Collectors.toList());
        if(currentCategories.contains(facilityCreateRequestDto.getCategory())){
            return "redirect:/admin";
        }

        // 이미지를 S3 에 추가
        MultipartFile image = facilityCreateRequestDto.getFacilityImage();
        String imageUrl = awsS3Uploader.upload(image, "facility");

        // Facility 저장
        Facility facility = Facility.builder()
                .facilityName(facilityCreateRequestDto.getFacilityName())
                .facilityImageUrl(imageUrl)
                .category(facilityCreateRequestDto.getCategory())
                .build();
        facilityRepository.save(facility);

        return "redirect:/admin";
    }

    public Facility findFacility(Long facilityId) {
        // 있는 facility 기준으로 출력하고, 해당 facility id 로 받아오는 것이므로, null 없다 가정
        return facilityRepository.findById(facilityId).get();
    }

    public void saveRoomFacility(RoomFacility roomFacility) {
        roomFacilityRepository.save(roomFacility);
    }
}
