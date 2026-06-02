package ktb.community.dto.post.request;

public record UpdatePostRequest(
        String title,
        String content,
        String postImageUrl
) {}
