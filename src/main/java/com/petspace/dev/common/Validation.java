package com.petspace.dev.common;

import com.petspace.dev.config.BaseException;
import com.petspace.dev.config.BaseResponseStatus;
import org.springframework.validation.FieldError;

public class Validation {

    public static BaseException validateUserRegisterRequestDto(FieldError error) {
        String errorField = error.getField();
        String errorMessage = error.getDefaultMessage();
        if (errorField.equals("nickname")) {
            return new BaseException(BaseResponseStatus.POST_USER_EMPTY_NICKNAME);
        }

        if (errorField.equals("username")) {
            return new BaseException(BaseResponseStatus.POST_USER_EMPTY_USERNAME);
        }

        if (errorField.equals("birth")) {
            return new BaseException(BaseResponseStatus.POST_USER_EMPTY_BIRTH);
        }

        if (errorField.equals("password")) {
            return new BaseException(BaseResponseStatus.POST_USER_EMPTY_PASSWORD);
        }

        if (errorField.equals("marketingAgreement")) {
            return new BaseException(BaseResponseStatus.POST_USER_EMPTY_MARKETING_AGREEMENT);
        }

        if (errorField.equals("email")) {
            if (errorMessage.equals("EMPTY_EMAIL")) {
                return new BaseException(BaseResponseStatus.POST_USER_EMPTY_EMAIL);
            }
            return new BaseException(BaseResponseStatus.POST_USER_INVALID_EMAIL);
        }
        return null;
    }
}
