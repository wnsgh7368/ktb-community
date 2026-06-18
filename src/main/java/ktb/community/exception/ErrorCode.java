package ktb.community.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 전역 에러 코드
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // User 에러 코드
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),

    // Auth 에러 코드
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),

    // Post 에러 코드
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 게시글을 찾을 수 없습니다."),

    // Like 에러 코드
    LIKE_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 좋아요가 등록된 상태입니다."),
    LIKE_ALREADY_CANCELED(HttpStatus.NOT_FOUND, "이미 좋아요가 취소되어 있는 상태입니다."),

    // Comment 에러 코드
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id의 댓글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
