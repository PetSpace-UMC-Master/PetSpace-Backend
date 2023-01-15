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

    // @Valid Exception Handler -> 실패시, REQUEST_ERROR Status
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse<Object> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("bindingResult.hasErrors()");
        List<BaseResponse.ValidationError> errors =
                e.getBindingResult().getFieldErrors().stream().map(BaseResponse.ValidationError::of).collect(Collectors.toList());
        // TODO errors를 담아서 모든 검증 오류를 내보낼지, 혹은 메시지만 내보낼지 프론트와 얘기하기
        return new BaseResponse<>(INVALID_INPUT, errors);
    }

    // UserException -> 중복된 이메일
    @ExceptionHandler({UserException.class})
    public BaseResponse<Object> handleUserException(UserException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
