package ktb.community.repository;

import ktb.community.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryCustom {
    List<Post> findPostsByCursor(Long cursor, int size);
}
