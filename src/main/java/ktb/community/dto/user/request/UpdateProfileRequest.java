package ktb.community.dto.user.request;

public record UpdateProfileRequest (
        String email,
        String nickname,
        String profileImageUrl
){}
