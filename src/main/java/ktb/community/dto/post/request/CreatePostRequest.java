package ktb.community.dto.post.request;

public record CreatePostRequest(
        String title,
        String content,
        String postImageUrl
) {}
