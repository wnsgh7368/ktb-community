package ktb.community.repository;

import ktb.community.entity.Like;
import ktb.community.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
