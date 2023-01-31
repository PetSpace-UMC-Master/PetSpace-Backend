package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class ReissueException extends RuntimeException {

    private final BaseResponseStatus status;

    public ReissueException(BaseResponseStatus status) {
        this.status = status;
    }
}
