package com.petspace.dev.util.exception.handler;

import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.BaseResponseStatus;
import com.petspace.dev.util.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.INVALID_INPUT;
import static com.petspace.dev.util.BaseResponseStatus.METHOD_ARGUMENT_TYPE_MISMATCH;

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

    @ExceptionHandler({ReviewException.class})
    public BaseResponse<Object> handleUserException(ReviewException e) {
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler({AwsException.class})
    public BaseResponse<Object> handleUserException(AwsException e) {
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public BaseResponse<Object> handlerRequestParam() {
        return new BaseResponse<>(METHOD_ARGUMENT_TYPE_MISMATCH);
    }
}
