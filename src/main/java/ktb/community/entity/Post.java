package ktb.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "post_img_url")
    private String postImageUrl;

    @Column(name = "view_count", nullable = false, columnDefinition = "int default 0")
    private int viewCount = 0;

    @Column(name = "like_count", nullable = false, columnDefinition = "int default 0")
    private int likeCount = 0;

    @Column(name = "comment_count", nullable = false, columnDefinition = "int default 0")
    private int commentCount = 0;

    public void increaseViewCount() {
        this.viewCount++;
    }
    public void increaseLikeCount() {
        this.likeCount++;
    }
    public void decreaseLikeCount() {
        this.likeCount--;
    }
    public void updatePost(String title, String content, String postImageUrl) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (postImageUrl != null) this.postImageUrl = postImageUrl;
    }
}
