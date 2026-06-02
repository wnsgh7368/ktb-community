package ktb.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    Long id;

    @Column(name = "title", nullable = false)
    String title;
    @Column(name = "content", nullable = false)
    String content;
    @Column(name = "profile_img_url")
    String postImageUrl;
    @Column(name = "view_count")
    int viewCount;
    @Column
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
    boolean isDeleted;
}
