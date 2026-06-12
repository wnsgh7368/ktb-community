package ktb.community.dto.post.response;

import ktb.community.dto.comment.response.GetCommentsResponse;
import ktb.community.entity.Post;

import java.time.LocalDateTime;

public record GetPostDetailResponse(
   Long postId,
   String title,
   String imageUrl,
   int likesCount,
   int viewsCount,
   int commentsCount,
   LocalDateTime createdAt,
   boolean isLiked,
   boolean isOwner,
   AuthorResponse author
) {
    public record AuthorResponse(
            String nickname,
            String profileImageUrl
    ){}

    public static GetPostDetailResponse of(Post post, boolean isLiked, boolean isOwner) {
        return new GetPostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getPostImageUrl(),
                post.getLikeCount(),
                post.getViewCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                isLiked,
                isOwner,
                new GetPostDetailResponse.AuthorResponse(
                        post.getUser().getNickname(),
                        post.getUser().getProfileImageUrl()
                ));
    }
}

/**
 * title": "제목",
 *     "content": "내용",
 *     "image_url": "https://s3.amazonaws.com/...",
 *     "likes_count": 10,
 *     "views_count": 22,
 *     "comments_count": 33,
 *     "created_at": "2026-05-23 00:00:00",
 *     "is_liked": true,      // 내가 좋아요 눌렀는지?
 *     "is_owner": true,      // 내가 작성자인지? (수정/삭제 버튼 표시용)
 *     "author": {
 *       "nickname": "작성자 1",
 *       "profile_image_url": "https://s3.amazonaws.com/..."
 *     }
 */