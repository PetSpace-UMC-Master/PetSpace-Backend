package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class ReservationException extends RuntimeException{

    private final BaseResponseStatus status;

    public ReservationException(BaseResponseStatus status) {
        this.status = status;
    }
}
