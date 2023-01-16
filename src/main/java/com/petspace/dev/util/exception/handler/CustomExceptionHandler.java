package com.petspace.dev.util.exception.handler;

import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.INVALID_INPUT;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse<Object> handleValidationException(MethodArgumentNotValidException e) {
        List<BaseResponse.ValidationError> errors = getFieldErrors(e);
        return new BaseResponse<>(INVALID_INPUT, errors);
    }

    @ExceptionHandler({UserException.class})
    public BaseResponse<Object> handleUserException(UserException e) {
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler({ReviewException.class})
    public BaseResponse<Object> handleUserException(ReviewException e) {
        return new BaseResponse<>(e.getStatus());
    }

    private List<BaseResponse.ValidationError> getFieldErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(BaseResponse.ValidationError::of)
                .collect(Collectors.toList());
    }
}
