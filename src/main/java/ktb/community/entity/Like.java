package ktb.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like {

    @EmbeddedId
    private LikeId likeId;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
