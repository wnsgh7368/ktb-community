package ktb.community.repository;

import ktb.community.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepositoryCustom {
    List<Comment> findCommentsByCursor(Long postId, Long cursor, int size);
}
