package ktb.community.exception;

import ktb.community.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 에러 핸들러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        // 1. CustomException 에서 enum(ErrorCode)를 꺼낸다.
        ErrorCode errorCode = e.getErrorCode();
        // 2. status -> errorCode의 status, // body -> errorCode
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode));
    }

    // Controller에서 @valid 실패시 내려주는 응답
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ValidExceptionResponse>>> handleValidException(MethodArgumentNotValidException e) {

        List<ValidExceptionResponse> details = e.getBindingResult().getFieldErrors().stream()
                .map(ValidExceptionResponse::of)
                .toList();

        return ResponseEntity
                .status(ErrorCode.INVALID_REQUEST.getStatus())
                .body(ApiResponse.error(ErrorCode.INVALID_REQUEST, details));
    }

    // 예기치 못한 서버 에러 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception e) {
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }

//    @ExceptionHandler(AccessDeniedException.class)
}
