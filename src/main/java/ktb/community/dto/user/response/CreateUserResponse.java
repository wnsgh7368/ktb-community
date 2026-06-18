package ktb.community.dto.user.response;

import ktb.community.entity.User;

public record CreateUserResponse(
        Long userId
) {
    public static CreateUserResponse of(User user) {
        return new CreateUserResponse(user.getId());
    }
}
