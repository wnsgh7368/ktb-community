package ktb.community.dto.user.request;

public record CreateUserRequest(
        String email,
        String password,
        String nickname,
        String profileImageUrl
) {}
