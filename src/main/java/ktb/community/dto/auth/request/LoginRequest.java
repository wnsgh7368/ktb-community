package ktb.community.dto.auth.request;

public record LoginRequest(
        String email,
        String password
) {}
