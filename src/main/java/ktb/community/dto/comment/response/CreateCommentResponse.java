package ktb.community.dto.comment.response;

public record CreateCommentResponse(
        Long postId,
        Long commentId
) {}
