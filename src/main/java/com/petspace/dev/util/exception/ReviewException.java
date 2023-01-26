package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class ReviewException extends RuntimeException{

    private final BaseResponseStatus status;

    public ReviewException(BaseResponseStatus status) {
        this.status = status;
    }
}
