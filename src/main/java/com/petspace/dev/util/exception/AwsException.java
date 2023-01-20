package com.petspace.dev.util.exception;

import com.petspace.dev.util.BaseResponseStatus;
import lombok.Getter;

@Getter
public class AwsException extends RuntimeException{

    private final BaseResponseStatus status;

    public AwsException(BaseResponseStatus status) {
        this.status = status;
    }
}
