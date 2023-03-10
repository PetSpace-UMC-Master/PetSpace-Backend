package com.petspace.dev.service;

import com.petspace.dev.domain.Address;
import com.petspace.dev.domain.Category;
import com.petspace.dev.domain.Facility;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.RoomAvailable;
import com.petspace.dev.domain.RoomCategory;
import com.petspace.dev.domain.RoomFacility;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.image.RoomImage;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.dto.admin.CategoryCreateRequestDto;
import com.petspace.dev.dto.admin.FacilityCreateRequestDto;
import com.petspace.dev.dto.admin.RoomCreateRequestDto;
import com.petspace.dev.dto.admin.RoomImageAddRequestDto;
import com.petspace.dev.repository.CategoryRepository;
import com.petspace.dev.repository.FacilityRepository;
import com.petspace.dev.repository.RoomAvailableRepository;
import com.petspace.dev.repository.RoomCategoryRepository;
import com.petspace.dev.repository.RoomFacilityRepository;
import com.petspace.dev.repository.RoomImageRepository;
import com.petspace.dev.repository.RoomRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        log.info("@@ findUsers");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findUserById(Long userId){
        log.info("@@ findUserById");
        // ?????? ?????? ???????????? ????????????, ?????? ?????? id ??? ???????????? ????????????, null ?????? ??????
        return userRepository.findById(userId).get();
    }

    /** Room ?????? */
    public void createRoom(Long userId, List<Long> facilitiesId, List<Long> categoriesId, RoomCreateRequestDto roomCreateRequestDto) {

        // ??????
        User user = findUserById(userId);

        // ??????
        Address address = new Address(roomCreateRequestDto.getRegion(), roomCreateRequestDto.getCity(), roomCreateRequestDto.getDistrict(), roomCreateRequestDto.getAddressDetail(),
                roomCreateRequestDto.getLatitude(), roomCreateRequestDto.getLongitude());

        // ????????????
        String roomName = roomCreateRequestDto.getRoomName();

        // ??????
        int price = roomCreateRequestDto.getPrice();

        // ?????? ?????????
        int maxGuest = roomCreateRequestDto.getMaxGuest();

        // ?????? ???????????????
        int maxPet = roomCreateRequestDto.getMaxPet();

        // ?????? ??????
        String description = roomCreateRequestDto.getDescription();

        // ?????????, ???????????? ?????? 2012-01-03T13:30:02
        // LocalDateTime ?????? ?????? ????????? ??? ????????? String ?????? ????????? ??? parsing HH:mm type To LocalDateTime with Default 2023-01-01
        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern("HH:mm"))
                .parseDefaulting(ChronoField.YEAR, 2023)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                // TODO ?????? 0 ?????????, 2023-01-01THH:mm ??? ss ?????? ????????????, ?????? ???????????? ???????????? ?
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

        // ???????????? ??????????????? RoomFacility save
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
            room.getRoomFacilities().add(roomFacility);
            log.info("?????? ??????");
        }

        // ???????????? ??????????????? RoomCategory save
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
            room.getRoomCategories().add(roomCategory);
            log.info("?????? ??????");
        }

    }

    /** ???????????? */
    @Transactional(readOnly = true)
    public List<Facility> findAllFacilities(){
        log.info("@@ findAllFacilities");
        return facilityRepository.findAll();
    }

    public String addFacility(FacilityCreateRequestDto facilityCreateRequestDto) {

        // ?????? ???????????? ?????????????????? ?????? X // TODO nonNull ????????? ???????????? ?????? ??????
        List<String> currentFacilities = findAllFacilities().stream()
                .filter(Objects::nonNull)
                .map(Facility::getFacilityName)
                .distinct()
                .collect(Collectors.toList());

        if(currentFacilities.contains(facilityCreateRequestDto.getFacilityName())){
            return "redirect:/admin";
        }

        // ???????????? S3 ??? ??????
        MultipartFile image = facilityCreateRequestDto.getFacilityImage();
        String imageUrl = awsS3Uploader.upload(image, "facility");
        log.info("?????? ?????? ??????");
        // Facility ??????
        Facility facility = Facility.builder()
                .facilityName(facilityCreateRequestDto.getFacilityName())
                .facilityImageUrl(imageUrl)
                .category(facilityCreateRequestDto.getCategory())
                .build();

        facilityRepository.save(facility);

        return "redirect:/admin";
    }

    @Transactional(readOnly = true)
    public Facility findFacility(Long facilityId) {
        // ?????? facility ???????????? ????????????, ?????? facility id ??? ???????????? ????????????, null ?????? ??????
        return facilityRepository.findById(facilityId).get();
    }

    public void saveRoomFacility(RoomFacility roomFacility) {
        roomFacilityRepository.save(roomFacility);
    }

    /** ???????????? */
    @Transactional(readOnly = true)
    public List<Category> findAllCategories(){
        log.info("@@ findAllCategories");
        return categoryRepository.findAll();
    }

    public String addCategory(CategoryCreateRequestDto categoryCreateRequestDto) {

        // ?????? ???????????? ?????????????????? ?????? X // TODO nonNull ????????? ???????????? ?????? ??????
        List<String> currentCategories = findAllCategories().stream()
                .filter(Objects::nonNull)
                .map(Category::getCategoryName)
                .distinct()
                .collect(Collectors.toList());
        if(currentCategories.contains(categoryCreateRequestDto.getCategoryName())){
            return "redirect:/admin";
        }

        // ???????????? S3 ??? ??????
        MultipartFile image = categoryCreateRequestDto.getCategoryImage();
        String imageUrl = awsS3Uploader.upload(image, "category");

        // Category ??????
        Category category = Category.builder()
                .categoryName(categoryCreateRequestDto.getCategoryName())
                .categoryImageUrl(imageUrl)
                .build();
        categoryRepository.save(category);

        return "redirect:/admin";
    }

    @Transactional(readOnly = true)
    public Category findCategory(Long categoryId) {
        // ?????? category ???????????? ????????????, ?????? category id ??? ???????????? ????????????, null ?????? ??????
        return categoryRepository.findById(categoryId).get();
    }

    // TODO saveRoomFacility ????????? RoomFacility ??? room ?????? CascadeType ALL ????????? ???????????????, ?????? ??? ??????
    public void saveRoomCategory(RoomCategory roomCategory) {
        roomCategoryRepository.save(roomCategory);
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRooms(){
        return roomRepository.findAll();
    }

    public void saveRoomAvailable(Long roomId, LocalDate available) {

        Room room = roomRepository.findById(roomId).get();

        List<LocalDate> localDateTimes = roomAvailableRepository.findAllByRoom(room)
                .stream().map(RoomAvailable::getAvailableDay).collect(Collectors.toList());

        if(localDateTimes.contains(available)) {
            log.info("?????? ???????????? ??????");
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

        // ???????????? S3 ??? ??????
        MultipartFile image = roomImageAddRequestDto.getRoomImage();
        String imageUrl = awsS3Uploader.upload(image, "rooms/"+roomId);

        RoomImage roomImage = RoomImage.builder()
                .room(room)
                .roomImageUrl(imageUrl)
                .build();

        roomImageRepository.save(roomImage);
    }

}
