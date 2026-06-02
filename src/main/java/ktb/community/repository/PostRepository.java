package ktb.community.repository;

import ktb.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.user where p.id = :postId")
    Optional<Post> findByIdWithUser(@Param("postId")Long postId);
}
