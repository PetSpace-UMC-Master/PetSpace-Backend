package com.petspace.dev.service;

import com.petspace.dev.domain.*;
import com.petspace.dev.domain.image.RoomImage;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.dto.admin.CategoryCreateRequestDto;
import com.petspace.dev.dto.admin.FacilityCreateRequestDto;
import com.petspace.dev.dto.admin.RoomCreateRequestDto;
import com.petspace.dev.dto.admin.RoomImageAddRequestDto;
import com.petspace.dev.repository.*;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
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
    private final CategoryRepository categoryRepository;
    private final RoomCategoryRepository roomCategoryRepository;
    private final RoomRepository roomRepository;
    private final RoomAvailableRepository roomAvailableRepository;
    private final RoomImageRepository roomImageRepository;
    private final AwsS3Uploader awsS3Uploader;

    public List<User> findAllUsers() {
        log.info("@@ findUsers");
        return userRepository.findAll();
    }

    public User findUserById(Long userId){
        log.info("@@ findUserById");
        // 있는 유저 기준으로 출력하고, 해당 유저 id 로 받아오는 것이므로, null 없다 가정
        return userRepository.findById(userId).get();
    }

    /** Room 생성 */
    public void createRoom(Long userId, List<Long> facilitiesId, List<Long> categoriesId, RoomCreateRequestDto roomCreateRequestDto) {

        // 유저
        User user = findUserById(userId);

        // 주소
        Address address = new Address(roomCreateRequestDto.getRegion(), roomCreateRequestDto.getCity(), roomCreateRequestDto.getDistrict(), roomCreateRequestDto.getAddressDetail(),
                roomCreateRequestDto.getLatitude(), roomCreateRequestDto.getLongitude());

        // 상세설명
        String roomName = roomCreateRequestDto.getRoomName();

        // 가격
        int price = roomCreateRequestDto.getPrice();

        // 최대 인원수
        int maxGuest = roomCreateRequestDto.getMaxGuest();

        // 최대 반려동물수
        int maxPet = roomCreateRequestDto.getMaxPet();

        // 숙소 상세
        String description = roomCreateRequestDto.getDescription();

        // 체크인, 체크아웃 시간 2012-01-03T13:30:02
        // LocalDateTime 으로 바로 받아올 수 없어서 String 으로 받아온 후 parsing HH:mm type To LocalDateTime with Default 2023-01-01
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("HH:mm"))
                .parseDefaulting(ChronoField.YEAR, 2023)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                // TODO 초가 0 일때는, 2023-01-01THH:mm 로 ss 출력 안되는데, 주언 쪽에서는 괜찮은지 ?
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();

        LocalDateTime checkinTime = LocalDateTime.parse(roomCreateRequestDto.getCheckinTime(), formatter);
        LocalDateTime checkoutTime = LocalDateTime.parse(roomCreateRequestDto.getCheckoutTime(), formatter);
        log.info("checkinTime {}", checkinTime);
        log.info("checkoutTime {}", checkoutTime);

        Room room = Room.builder()
                .user(user)
                .address(address)
                .roomName(roomName)
                .price(price)
                .maxGuest(maxGuest)
                .maxPet(maxPet)
                .description(description)
                .checkinTime(checkinTime)
                .checkoutTime(checkoutTime)
                .status(Status.ACTIVE)
                .build();

        // 편의시설 중간테이블 RoomFacility save
        List<Facility> facilities = facilitiesId.stream()
                .map(facilityId -> findFacility(facilityId))
                .collect(Collectors.toList());

        for(Facility facility : facilities){
            RoomFacility roomFacility = RoomFacility.builder()
                    .room(room)
                    .facility(facility)
                    .build();
            log.info("roomFacility id : {}", roomFacility.getId());
            log.info("roomFacility room name : {}", roomFacility.getRoom().getRoomName());
            log.info("roomFacility facility name : {}", roomFacility.getFacility().getFacilityName());
            saveRoomFacility(roomFacility);
            log.info("저장 성공");
        }

        // 카테고리 중간테이블 RoomCategory save
        List<Category> categories = categoriesId.stream()
                .map(categoryId -> findCategory(categoryId))
                .collect(Collectors.toList());

        for(Category category : categories){
            RoomCategory roomCategory = RoomCategory.builder()
                    .room(room)
                    .category(category)
                    .build();
            log.info("roomCategory id : {}", roomCategory.getId());
            log.info("roomCategory room name : {}", roomCategory.getRoom().getRoomName());
            log.info("roomCategory category name : {}", roomCategory.getCategory().getCategoryName());
            saveRoomCategory(roomCategory);
            log.info("저장 성공");
        }

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

    /** 카테고리 */
    public List<Category> findAllCategories(){
        log.info("@@ findAllCategories");
        return categoryRepository.findAll();
    }

    public String addCategory(CategoryCreateRequestDto categoryCreateRequestDto) {

        // 이미 존재하는 카테고리라면 추가 X // TODO nonNull 적절한 위치인지 체크 필요
        List<String> currentCategories = findAllCategories().stream()
                .filter(Objects::nonNull)
                .map(Category::getCategoryName)
                .distinct()
                .collect(Collectors.toList());
        if(currentCategories.contains(categoryCreateRequestDto.getCategoryName())){
            return "redirect:/admin";
        }

        // 이미지를 S3 에 추가
        MultipartFile image = categoryCreateRequestDto.getCategoryImage();
        String imageUrl = awsS3Uploader.upload(image, "category");

        // Category 저장 TODO 따로 뺄까
        Category category = Category.builder()
                .categoryName(categoryCreateRequestDto.getCategoryName())
                .categoryImageUrl(imageUrl)
                .build();
        categoryRepository.save(category);

        return "redirect:/admin";
    }

    public Category findCategory(Long categoryId) {
        // 있는 category 기준으로 출력하고, 해당 category id 로 받아오는 것이므로, null 없다 가정
        return categoryRepository.findById(categoryId).get();
    }

    // TODO saveRoomFacility 시에는 RoomFacility 의 room 컬럼 CascadeType ALL 안주면 에러났는데, 여긴 왜 되지
    public void saveRoomCategory(RoomCategory roomCategory) {
        roomCategoryRepository.save(roomCategory);
    }

    public List<Room> findAllRooms(){
        return roomRepository.findAll();
    }

    public void saveRoomAvailable(Long roomId, LocalDateTime available) {

        Room room = roomRepository.findById(roomId).get();

        List<LocalDateTime> localDateTimes = roomAvailableRepository.findAllByRoom(room)
                .stream().map(RoomAvailable::getAvailableDay).collect(Collectors.toList());

        if(localDateTimes.contains(available)) {
            log.info("이미 존재하는 정보");
            return;
        }

        RoomAvailable roomAvailable = RoomAvailable.builder()
                .room(room)
                .availableDay(available)
                .status(Status.ACTIVE)
                .build();

        roomAvailableRepository.save(roomAvailable);

    }

    public void addRoomImage(Long roomId, RoomImageAddRequestDto roomImageAddRequestDto) {

        Room room = roomRepository.findById(roomId).get();

        // 이미지를 S3 에 추가
        MultipartFile image = roomImageAddRequestDto.getRoomImage();
        String imageUrl = awsS3Uploader.upload(image, "rooms/"+roomId);

        RoomImage roomImage = RoomImage.builder()
                .room(room)
                .roomImageUrl(imageUrl)
                .build();

        roomImageRepository.save(roomImage);
    }

}
