package com.petspace.dev.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // 입력값 예외 (검증)
    INVALID_INPUT(false, 2000, "잘못된 입력이 존재합니다."),

    // JWT 예외
    EXPIRED_JWT(false, 2001, "이미 만료된 토큰입니다."),
    UNSUPPORTED_TOKEN_TYPE(false, 2002, "지원되지 않는 토큰 형식입니다."),
    MALFORMED_TOKEN_TYPE(false, 2003, "인증 토큰이 올바르게 구성되지 않았습니다."),
    INVALID_SIGNATURE_JWT(false, 2004, "인증 시그니처가 올바르지 않습니다"),
    INVALID_TOKEN_TYPE(false, 2005, "잘못된 토큰입니다."),
    ACCESS_DENIED(false, 2006, "권한이 없는 유저의 접근입니다."),

    // UserException
    DUPLICATED_EMAIL(false, 2010, "중복된 이메일입니다."),
    INVALID_CHECKED_PASSWORD(false, 2011, "비밀번호 확인 값이 다릅니다."),
    INVALID_EMAIL_OR_PASSWORD(false, 2012, "이메일 혹은 비밀번호가 잘못되었습니다."),
    NONE_USER(false, 2013, "존재하지 않는 회원입니다."),


    // ReviewException
    POST_REVIEW_EMPTY_USER(false, 2020, "해당 사용자가 존재하지 않습니다."),
    POST_REVIEW_EMPTY_RESERVATION(false, 2021, "해당 예약이 존재하지 않습니다."),
    POST_REVIEW_EMPTY_SCORE(false, 2022, "score를 입력해주세요."),

    // ReservationException
    POST_RESERVATION_EMPTY_USER(false, 2030, "해당 사용자가 존재하지 않습니다."),
    POST_RESERVATION_EMPTY_ROOM(false, 2031, "해당 숙소가 존재하지 않습니다."),
    POST_RESERVATION_INVALID_ROOM_STATUS(false, 2032, "해당 숙소가 예약 불가능한 상태입니다."),
    PATCH_RESERVATION_INVALID_RESERVATION_STATUS(false, 2033, "유효하지 않은 예약입니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    /**
     * 5000 : AWS 오류
     */
    // AWS S3
    POST_IMAGE_CONVERT_ERROR(false, 5000, "사진이 없거나 변환되지 않았습니다."),
    POST_IMAGE_INVALID_EXTENSION(false, 5001, "올바른 확장자가 아닙니다.");


    private final boolean isSuccess;
    private final int responseCode;
    private final String responseMessage;
}
