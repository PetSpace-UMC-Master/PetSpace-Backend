package com.petspace.dev.controller;

import com.petspace.dev.domain.Category;
import com.petspace.dev.domain.Facility;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.dto.admin.CategoryCreateRequestDto;
import com.petspace.dev.dto.admin.FacilityCreateRequestDto;
import com.petspace.dev.dto.admin.RoomCreateRequestDto;
import com.petspace.dev.dto.admin.RoomImageAddRequestDto;
import com.petspace.dev.dto.user.UserJoinRequestDto;
import com.petspace.dev.service.AdminService;
import com.petspace.dev.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public String userCheck(Model model){
        List<User> users = adminService.findAllUsers();
        model.addAttribute("users", users);
        return "users/userList";
    }

    /** 숙소 생성 **/
    @GetMapping("/rooms/new")
    public String createRoomReq(Model model){

        log.info("@@ createRoomReq");
        // 유저
        List<User> users = adminService.findAllUsers();
        List<Facility> facilities = adminService.findAllFacilities();
        List<Category> categories = adminService.findAllCategories();
        model.addAttribute("users", users);
        model.addAttribute("facilities", facilities);
        model.addAttribute("categories", categories);
        model.addAttribute("roomCreateRequestDto", new RoomCreateRequestDto());

        return "rooms/roomCreateForm";
    }

    @PostMapping("/rooms/new")
    public String createRoom(@RequestParam("userId") Long userId, @RequestParam("facilitiesId") List<Long> facilitiesId, @RequestParam("categoriesId") List<Long> categoriesId, @Valid RoomCreateRequestDto roomCreateRequestDto){

        adminService.createRoom(userId, facilitiesId, categoriesId, roomCreateRequestDto);

        return "redirect:/admin";
    }

    /** 숙소 조회 **/
    @GetMapping("/rooms")
    public String roomCheck(Model model){

        List<Room> rooms = adminService.findAllRooms();
        model.addAttribute("rooms", rooms);

        return "rooms/roomList";
    }


    /** 편의시설 추가 */
    @GetMapping("/facilities/new")
    public String addFacility(Model model){

        model.addAttribute("facilityCreateRequestDto", new FacilityCreateRequestDto());
        return "facilities/addFacilityForm";
    }

    @PostMapping("/facilities/new")
    public String addFacility(@Valid FacilityCreateRequestDto facilityCreateRequestDto){

        log.info("getCategory = {}", facilityCreateRequestDto.getCategory());
        log.info("getFacilityName = {}", facilityCreateRequestDto.getFacilityName());
        log.info("getFacilityImage = {}", facilityCreateRequestDto.getFacilityImage());

        String result = adminService.addFacility(facilityCreateRequestDto);

        return result;
    }

    /** 숙소 카테고리 추가 */
    @GetMapping("/categories/new")
    public String addCategory(Model model){

        model.addAttribute("categoryCreateRequestDto", new CategoryCreateRequestDto());
        return "categories/addCategoryForm";
    }

    @PostMapping("/categories/new")
    public String addCategory(@Valid CategoryCreateRequestDto categoryCreateRequestDto){

        String result = adminService.addCategory(categoryCreateRequestDto);

        return result;
    }

    /** 숙소 가능한 시간 추가 */
    @GetMapping("/rooms/availables")
    public String addAvailables(Model model){

        List<Room> rooms = adminService.findAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("date", new String());

        return "rooms/roomAvailableForm";
    }

    @PostMapping("/rooms/availables")
    public String addAvailables(@RequestParam("roomId") Long roomId, @RequestParam("date") @DateTimeFormat(pattern = "MM-dd-yyyy") String date){

        log.info("date is {}", date);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate available = LocalDate.parse(date, formatter);

        log.info("available date is {}", available);

        adminService.saveRoomAvailable(roomId, available);

        return "redirect:/admin/rooms/availables";
    }

    /** 숙소 이미지 추가 */
    @GetMapping("/rooms/image")
    public String addRoomImage(Model model){

        List<Room> rooms = adminService.findAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("roomImageAddRequestDto", new RoomImageAddRequestDto());

        return "rooms/roomImageAddForm";
    }

    @PostMapping("/rooms/image")
    public String addRoomImage(@RequestParam("roomId") Long roomId, RoomImageAddRequestDto roomImageAddRequestDto){

        adminService.addRoomImage(roomId, roomImageAddRequestDto);

        return "redirect:/admin/rooms/image";
    }

}
