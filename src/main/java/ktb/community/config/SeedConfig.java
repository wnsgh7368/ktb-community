package ktb.community.config;

import ktb.community.entity.Post;
import ktb.community.entity.User;
import ktb.community.repository.PostRepository;
import ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class SeedConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner seedRunner() {
        return args -> seed();
    }

    @Transactional
    void seed() {
        // 이미 시드 돌렸으면 중복 방지
        if (userRepository.count() >= 5) return;

        // User 5명
        List<User> users = new ArrayList<>();
        IntStream.rangeClosed(1, 5).forEach(i ->
                users.add(userRepository.save(User.builder()
                        .email("user" + i + "@test.com")
                        .nickname("user-" + i)
                        .password(passwordEncoder.encode("password" + i))
                        .profileImageUrl("https://picsum.photos/seed/u" + i + "/100")
                        .build()))
        );

        // Post 30개 — 작성자 라운드로빈으로 분배 (페이지네이션 테스트용)
        IntStream.rangeClosed(1, 30).forEach(i -> {
            User author = users.get((i - 1) % users.size());
            postRepository.save(Post.builder()
                    .user(author)
                    .title("게시글 " + i)
                    .content("게시글 " + i + "의 내용입니다.")
                    .postImageUrl("https://picsum.photos/seed/p" + i + "/600/400")
                    .viewCount(ThreadLocalRandom.current().nextInt(0, 500))
                    .likeCount(ThreadLocalRandom.current().nextInt(0, 100))
                    .commentCount(0)
                    .build());
        });
    }
}