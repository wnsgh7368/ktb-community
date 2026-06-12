package ktb.community.dto.comment.response;

import ktb.community.entity.Comment;

public record UpdateCommentResponse(
        Long commentId
) {
    public static UpdateCommentResponse of(Comment comment) {
        return new UpdateCommentResponse(comment.getId());
    }
}
