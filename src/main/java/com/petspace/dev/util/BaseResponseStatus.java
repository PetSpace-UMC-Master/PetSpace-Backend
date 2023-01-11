package com.petspace.dev.util;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, 2003, "권한이 없는 유저의 접근입니다."),

    // user
    EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /user
    POST_USER_EMPTY_EMAIL(false, 2011, "이메일을 입력해주세요."),
    POST_USER_INVALID_EMAIL(false, 2012, "이메일 형식을 확인해주세요."),
    POST_USER_EXISTS_EMAIL(false, 2013, "중복된 이메일입니다."),
    POST_USER_EMPTY_NICKNAME(false, 2014, "닉네임을 입력해주세요."),
    POST_USER_EXISTS_NICKNAME(false, 2015, "중복된 닉네임입니다."),
    POST_USER_EMPTY_USERNAME(false, 2016, "사용자 이름을 입력해주세요."),
    POST_USER_EMPTY_BIRTH(false, 2017, "생년월일을 입력해주세요."),
    POST_USER_EMPTY_PASSWORD(false, 2018, "비밀번호을 입력해주세요."),
    POST_USER_EMPTY_MARKETING_AGREEMENT(false, 2019, "마케팅 이용 동의 여부를 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.");

    private final boolean isSuccess;
    private final int responseCode;
    private final String responseMessage;

    private BaseResponseStatus(boolean isSuccess, int responseCode, String responseMessage) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
