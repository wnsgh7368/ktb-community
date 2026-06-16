package ktb.community.exception;

import org.springframework.validation.FieldError;

public record FieldErrorDetail(String field, String reason) {
    public static FieldErrorDetail of(FieldError e) {
        return new FieldErrorDetail(e.getField(), e.getDefaultMessage());
    }
}
