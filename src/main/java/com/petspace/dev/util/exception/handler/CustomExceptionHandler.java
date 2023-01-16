package com.petspace.dev.util.exception.handler;

import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.petspace.dev.util.BaseResponseStatus.INVALID_INPUT;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse<Object> handleValidationException() {
        return new BaseResponse<>(INVALID_INPUT);
    }

    @ExceptionHandler({UserException.class})
    public BaseResponse<Object> handleUserException(UserException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
