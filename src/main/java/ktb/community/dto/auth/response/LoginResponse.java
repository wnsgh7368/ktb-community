package ktb.community.dto.auth.response;

public record LoginResponse(
        Long userId,
        String accessToken
) {}
