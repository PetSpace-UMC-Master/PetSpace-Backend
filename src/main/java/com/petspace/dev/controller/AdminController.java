package com.petspace.dev.controller;

import com.petspace.dev.domain.Address;
import com.petspace.dev.domain.Room;
import com.petspace.dev.dto.admin.RoomCreateRequestDto;
import com.petspace.dev.dto.user.UserJoinRequestDto;

import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.service.AdminService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @RequestMapping("")
    public String home(){
        return "home";
    }

    /** 회원 가입 **/
    @GetMapping("/users/new")
    public String createUserDto(Model model){
        model.addAttribute("userJoinRequestDto", new UserJoinRequestDto());
        return "users/createUserJoinRequestDto";
    }

    @PostMapping("/users/new")
    public String createUser(@Valid UserJoinRequestDto userJoinRequestDto, BindingResult result){
        userService.join(userJoinRequestDto);
        return "redirect:/admin";
    }

    /** 회원 조회 **/
    @GetMapping("/users")
    public String list(Model model){
        List<User> users = adminService.findUsers();
        model.addAttribute("users", users);
        return "users/userList";
    }

    /** 숙소 생성 **/
    @GetMapping("/rooms/new")
    public String createRoomReq(Model model){

        /**
         * 유저 선택해야하고, -> 완
         * 카테고리 ?
         * 편의시설 ?
         * 가능한 시간 ?
         * 룸 이미지 ?
         * 주소 Address 타입 -> 완
         * 숙소 이름 -> 완
         * 가격 -> 완
         * 최대게스트 -> 완
         * 최대펫 -> 완
         * 상세설명 -> 완
         * 체크인타임 -> 완
         * 체크아웃타임 -> 완
         * 상태 -> Active
         */
        log.info("@@ createRoomReq");
        // 유저
        List<User> users = adminService.findUsers();
        model.addAttribute("users", users);
        model.addAttribute("roomCreateRequestDto", new RoomCreateRequestDto());

        return "rooms/roomCreateForm";
    }

    @PostMapping("/rooms/new")
    public String createRoom(@RequestParam("userId") Long userId, @Valid RoomCreateRequestDto roomCreateRequestDto, BindingResult result){
        log.info("@@ selected userId is {}", userId);
        log.info("@@ RoomCreatedRequest : region is a{}a", roomCreateRequestDto.getRegion());
        log.info("@@ RoomCreatedRequest : city is {}", roomCreateRequestDto.getCity());
        log.info("@@ RoomCreatedRequest : district is {}", roomCreateRequestDto.getDistrict());
        log.info("@@ RoomCreatedRequest : addressDetail is {}", roomCreateRequestDto.getAddressDetail());
        log.info("@@ RoomCreatedRequest : latitude is {}", roomCreateRequestDto.getLatitude());
        log.info("@@ RoomCreatedRequest : longitude is {}", roomCreateRequestDto.getLongitude());

        log.info("@@ RoomCreatedRequest : roomName is {}", roomCreateRequestDto.getRoomName());
        log.info("@@ RoomCreatedRequest : price is {}", roomCreateRequestDto.getPrice());
        log.info("@@ RoomCreatedRequest : maxGuest is {}", roomCreateRequestDto.getMaxGuest());
        log.info("@@ RoomCreatedRequest : maxPet is {}", roomCreateRequestDto.getMaxPet());
        log.info("@@ RoomCreatedRequest : description is {}", roomCreateRequestDto.getDescription());
        log.info("@@ RoomCreatedRequest : checkinTime is {}", roomCreateRequestDto.getCheckinTime());
        log.info("@@ RoomCreatedRequest : checkoutTime is {}", roomCreateRequestDto.getCheckoutTime());

        // 유저
        User user = adminService.findUserById(userId);

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

        log.info("!!!!!!! {}", checkinTime);
        log.info("!!!!!!! {}", checkoutTime);

        // TODO 카테고리 -> 체크박스
        // TODO 편의시설 -> 체크박스
        // TODO 가능한 시간 -> 달력
        // TODO 룸 이미지 -> 파일 업로드 및 S3 저장
        // TODO 숙소 상태 -> 하드코딩

        // TODO 생성자에 파라미터로 필요한 정보들 넘겨주기
//        Room room = new Room();

        return "redirect:/admin";
    }

}
