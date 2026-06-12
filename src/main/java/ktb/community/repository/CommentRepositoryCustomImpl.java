package ktb.community.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ktb.community.entity.Comment;
import lombok.RequiredArgsConstructor;
import static ktb.community.entity.QComment.comment;
import static ktb.community.entity.QPost.post;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findCommentsByCursor(Long postId, Long cursor, int size) {
        return jpaQueryFactory
                .selectFrom(comment)
                .join(comment.user).fetchJoin()
                .where(comment.post.id.eq(postId),
                        cursorLt(cursor))
                .orderBy(comment.id.desc())
                .limit(size)
                .fetch();
    }

    // cursor에 null 값이 들어올 경우 처리 (초기 cursor값 처리) -> .where 절에서 null 값은 무시되서 정렬후 초기 값 반환
    private BooleanExpression cursorLt(Long cursor) {
        return cursor == null ? null : comment.id.lt(cursor);
    }
}
