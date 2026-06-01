package ktb.community.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        // 1. Throwable의 message 필드에 errorCode의 메세지 전달
        super(errorCode.getMessage());
        // 2. CustomException의 errorCode 필드에 errorCode 저장
        this.errorCode = errorCode;
    }
}
