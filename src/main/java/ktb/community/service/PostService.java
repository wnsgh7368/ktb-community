package ktb.community.service;

import jakarta.transaction.Transactional;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.request.UpdatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.dto.post.response.GetPostDetailResponse;
import ktb.community.dto.post.response.UpdatePostResponse;
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
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    /**
     * 게시글 생성 API 비즈니스 로직
     */
    @Transactional
    public CreatePostResponse createPost(Long userId, CreatePostRequest req) {

        // userId 로 게시글을 작성하는 유저가 존재하는지 찾는 로직 없으면 -> USER_NOT_FOUND
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // Post entity 객체 생성
        Post post = Post.builder()
                .user(user)
                .title(req.title())
                .content(req.content())
                .postImageUrl(req.postImageUrl())
                .build();
        // post entity 저장 후 postId return
        Post saved = postRepository.save(post);

        return new CreatePostResponse(saved.getId());
    }

    /**
     * 게시글 상세조회 API 비즈니스 로직
     * TODO: (고민) 하나의 조회 로직? 비즈니스 로직에 post 조회, post.viewCount 쓰기(+1), like 테이블 조회 3가지 I/O 과정?이 있다..
     */
    @Transactional
    public GetPostDetailResponse getPostDetail(Long userId, Long postId) {

        // postId로 게시글 조회 없으면 -> POST_NOT_FOUND
        // 조회시, fetch join으로 User를 함께 조회해서 N+1 문제 방지
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        // 조회를 함과 동시에 뷰 카운트 1 증가
        post.increaseViewCount();
        // (postId, userId) 복합키로 조회한 유저가 조회된 게시글에 좋아요를 눌렀는지 조회
        boolean isLiked = likeRepository.existsById(new LikeId(postId, userId));
        // 조회한 유저가 조회된 게시글의 주인인지 조회
        boolean isOwner = post.getUser().getId().equals(userId);

        // 응답 DTO 생성
        return new GetPostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getPostImageUrl(),
                post.getLikeCount(),
                post.getViewCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                isLiked,
                isOwner,
                new GetPostDetailResponse.AuthorResponse(
                        post.getUser().getNickname(),
                        post.getUser().getProfileImageUrl()
                ));
    }

    /**
     * 게시글 수정 API 비즈니스 로직
     */
    @Transactional
    public UpdatePostResponse updatePost(Long userId, Long postId, UpdatePostRequest req) {

        // postId로 게시글 조회 없으면 -> POST_NOT_FOUND
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 수정 권한 판별하기 post의 userId와 클라이언트의 userId 비교 -> 예외: FORBIDDEN
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 엔티티 메서드 updatePost를 통해 해당 엔티티를 수정하고, 이후 트랜잭션이 끝날 때 더티체킹으로 저장
        post.updatePost(req.title(), req.content(), req.postImageUrl());

        return new UpdatePostResponse(post.getId());
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 삭제 권한 판별하기 post의 userId와 클라이언트의 userId 비교 -> 예외: FORBIDDEN
        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        postRepository.delete(post);
    }

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
