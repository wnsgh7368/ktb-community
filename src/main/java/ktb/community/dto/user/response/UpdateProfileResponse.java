package ktb.community.dto.user.response;

import ktb.community.entity.User;

public record UpdateProfileResponse (
        Long userId
) {
    public static UpdateProfileResponse of(User user) {
        return new UpdateProfileResponse(user.getId());
    }
}
