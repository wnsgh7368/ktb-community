package ktb.community.service;

import jakarta.transaction.Transactional;
import ktb.community.entity.Like;
import ktb.community.entity.LikeId;
import ktb.community.entity.Post;
import ktb.community.entity.User;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.repository.LikeRepository;
import ktb.community.repository.PostRepository;
import ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 좋아요 등록 API 비즈니스 로직
     * 좋아요 등록시 post의 like_count도 증가
     */
    @Transactional
    public void createLike(Long userId, Long postId) {

        // 이미 좋아요를 눌렀는지 확인
        LikeId likeId = new LikeId(postId, userId);
        if (likeRepository.existsById(likeId)) {
            throw new CustomException(ErrorCode.LIKE_ALREADY_REGISTERED);
        }

        // post와 user의 유효성 검증
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // post의 like_count 증가
        post.increaseLikeCount();

        Like like = Like.builder()
                .likeId(likeId)
                .post(post)
                .user(user)
                .build();

        likeRepository.save(like);
    }

    /**
     * 좋아요 등록 취소 API 비즈니스 로직
     * 취소시 post의 like_count도 감소
     */
    @Transactional
    public void deleteLike(Long userId, Long postId) {

        // 해당 id의 게시물이 있는지 먼저 체크
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 좋아요가 이미 없는지 확인
        LikeId likeId = new LikeId(postId, userId);
        if (!likeRepository.existsById(likeId)) {
            throw new CustomException(ErrorCode.LIKE_ALREADY_CANCELED);
        }

        post.decreaseLikeCount(); // post의 like_count 감소
        likeRepository.deleteById(likeId); // like_id로 삭제
    }
}
