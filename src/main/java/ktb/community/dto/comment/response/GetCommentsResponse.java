package ktb.community.dto.comment.response;

import ktb.community.entity.Comment;

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

    public static GetCommentsResponse of(List<Comment> comments, Long nextCursor, boolean hasNext, Long userId) {

        List<CommentSummary> summaries = comments.stream()
                .map(c -> new CommentSummary(
                        c.getId(),
                        c.getContent(),
                        c.getCreatedAt(),
                        c.getUser().getId().equals(userId),
                        new CommentSummary.Author(
                                c.getUser().getNickname(),
                                c.getUser().getProfileImageUrl()
                        )
                )).toList();

        return new GetCommentsResponse(summaries, nextCursor, hasNext);
    }
}
