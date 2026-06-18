package ktb.community.dto.post.response;

import ktb.community.entity.Post;

public record CreatePostResponse(
   Long postId
) {
    public static CreatePostResponse of(Post post) {
        return new CreatePostResponse(post.getId());
    }
}
