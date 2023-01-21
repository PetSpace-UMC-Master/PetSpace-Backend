package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class RoomException extends RuntimeException{

    private final BaseResponseStatus status;

    public RoomException(BaseResponseStatus status) {
        this.status = status;
    }

}
