package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

    private final BaseResponseStatus status;

    public UserException(BaseResponseStatus status) {
        this.status = status;
    }
}
