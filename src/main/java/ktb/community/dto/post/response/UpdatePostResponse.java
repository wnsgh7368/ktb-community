package ktb.community.dto.post.response;

import ktb.community.entity.Post;

public record UpdatePostResponse(
   Long postId
) {
    public static UpdatePostResponse of(Post post) {
        return new UpdatePostResponse(post.getId());
    }
}
