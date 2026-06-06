package ktb.community.dto.comment.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetCommentsResponse(
    List<CommentSummary> comments,
    Long nextCursor,
    boolean hasNext
) {
    public record CommentSummary(
            Long commentId,
            String content,
            LocalDateTime createdAt,
            boolean isOwner,
            Author author
    ) {
        public record Author(
                String nickname,
                String profileImageUrl
        ){}
    }
}
