package com.petspace.dev.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import static com.petspace.dev.util.BaseResponseStatus.SUCCESS;

@Getter
@JsonPropertyOrder({"isSuccess", "responseCode", "responseMessage", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final int responseCode;
    private final String responseMessage;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final T result;

    // Request Success, Request 성공시 무조건 result 존재
    public BaseResponse(T result){
        this.isSuccess = SUCCESS.isSuccess();
        this.responseMessage = SUCCESS.getResponseMessage();
        this.responseCode = SUCCESS.getResponseCode();
        this.result = result;
    }

    // Common Exception, 보내는 데이터 없이 메시지만 출력하면 되는 에러 처리
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.responseMessage = status.getResponseMessage();
        this.responseCode = status.getResponseCode();
        this.result = null;
    }

    // Validation Exception, 형식 검증에서 여러 fieldError 들을 result 에 담아서 보내는 에러 처리
    public BaseResponse(BaseResponseStatus status, T result) {
        this.isSuccess = status.isSuccess();
        this.responseMessage = status.getResponseMessage();
        this.responseCode = status.getResponseCode();
        this.result = result;
    }

    @Getter
    @Builder
    public static class ValidationError {
        private final String field;
        private final String message;

        public static BaseResponse.ValidationError of(final FieldError fieldError) {
            return BaseResponse.ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}
