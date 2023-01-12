package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.review.ReviewRequestDto;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.petspace.dev.util.BaseResponseStatus.INVALID_USER_EXIST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long save(Long userId, Long reservationId, Review review) {

        Optional<User> user =  userRepository.findById(userId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        // toDo : 예외처리
        // toDo : JWT 토큰 확인

        return reviewRepository.save(review).getId();


//        // 엔티티 조회
//        Optional<User> user = userRepository.findByEmail(email);
//        Optional<Room> room = roomRepository.findById(roomId);
//
//        // 사진리스트정보 생성
////        RoomImage roomImage = RoomImage.
//
//        // 주문상품 생성
//        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
//        // OrderItem orderItem1 = new OrderItem(); // => 기본 생성자 protected되어 막음
//        // 또는 Order에 NoArgsConstructor 어노테이션 붙임
//
//        // 리뷰 생성
//        reviewRepository.save(re)
//        Order order = Order.createOrder(member, delivery, orderItem);
//        userRepository.save(user);
//        return user.getId();
//        // 주문 저장
//        orderRepository.save(order);


    }

}

