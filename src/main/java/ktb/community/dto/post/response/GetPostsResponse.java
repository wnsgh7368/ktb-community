package ktb.community.dto.post.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostsResponse(
        List<PostSummary> posts,
        Long nextCursor,
        boolean hasNext
) {
    public record PostSummary(
            Long postId,
            String title,
            int likeCount,
            int commentCount,
            int viewCount,
            LocalDateTime createdAt,
            Author author
    ) {
        public record Author(
           String nickname,
           String profileImageUrl
        ) {}
    }
}
