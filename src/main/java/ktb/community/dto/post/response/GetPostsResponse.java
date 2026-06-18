package ktb.community.dto.post.response;

import ktb.community.entity.Post;

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

    public static GetPostsResponse of(List<Post> posts, Long nextCursor, boolean hasNext) {
        List<PostSummary> summaries = posts.stream()
                .map(p -> new PostSummary(
                        p.getId(),
                        p.getTitle(),
                        p.getLikeCount(),
                        p.getCommentCount(),
                        p.getViewCount(),
                        p.getCreatedAt(),
                        new PostSummary.Author(
                                p.getUser().getNickname(),
                                p.getUser().getProfileImageUrl()
                        )
                )).toList();

        return new GetPostsResponse(summaries, nextCursor, hasNext);
    }
}
