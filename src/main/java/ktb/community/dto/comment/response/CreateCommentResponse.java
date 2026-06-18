package ktb.community.dto.comment.response;

import ktb.community.entity.Comment;
import ktb.community.entity.Post;

public record CreateCommentResponse(
        Long postId,
        Long commentId
) {
    public static CreateCommentResponse of(Post post, Comment comment) {
        return new CreateCommentResponse(post.getId(), comment.getId());
    }
}
