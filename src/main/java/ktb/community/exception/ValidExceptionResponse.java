package ktb.community.exception;

import org.springframework.validation.FieldError;

public record ValidExceptionResponse(
        String field,
        String reason
) {
    public static ValidExceptionResponse of(FieldError e) {
        return new ValidExceptionResponse(e.getField(), e.getDefaultMessage());
    }
}
