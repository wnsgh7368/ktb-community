package ktb.community.dto.auth.response;

import ktb.community.entity.User;

public record LoginResponse(
        Long userId,
        String accessToken
) {
    public static LoginResponse of(User user, String accessToken) {
        return new LoginResponse(user.getId(), accessToken);
    }
}
