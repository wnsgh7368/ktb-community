package ktb.community.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ktb.community.entity.Post;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static ktb.community.entity.QPost.post;
import static ktb.community.entity.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsByCursor(Long cursor, int size) {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(cursorLt(cursor))
                .orderBy(post.id.desc())
                .limit(size)
                .fetch();
    }

    private BooleanExpression cursorLt(Long cursor) {
        return cursor == null ? null : post.id.lt(cursor);
    }
}
