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

    // Request Success
    public BaseResponse(T result){
        this.isSuccess = SUCCESS.isSuccess();
        this.responseMessage = SUCCESS.getResponseMessage();
        this.responseCode = SUCCESS.getResponseCode();
        this.result = result;
    }

    // Common Exception
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.responseMessage = status.getResponseMessage();
        this.responseCode = status.getResponseCode();
        this.result = null;
    }

    // TODO 프론트와 협의 후, 만약 에러메시지만 출력되도록 한다면, 이 아래로 모두 지워도 괜찮
    // Validation Exception
    public BaseResponse(BaseResponseStatus status, T result) {
        this.isSuccess = status.isSuccess();
        this.responseMessage = status.getResponseMessage();
        this.responseCode = status.getResponseCode();
        this.result = result;
    }

    // @Valid 예외 발생시, 여러 예외를 던져줌
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
