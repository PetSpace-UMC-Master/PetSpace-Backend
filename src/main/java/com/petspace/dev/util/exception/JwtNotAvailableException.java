package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class JwtNotAvailableException extends RuntimeException {

    private final BaseResponseStatus status;

    public JwtNotAvailableException(BaseResponseStatus status) {
        this.status = status;
    }
}
