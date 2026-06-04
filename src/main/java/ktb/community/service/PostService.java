package ktb.community.service;

import jakarta.transaction.Transactional;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.request.UpdatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.dto.post.response.GetPostDetailResponse;
import ktb.community.dto.post.response.UpdatePostResponse;
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
        post.increseViewCount();
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
     * 더티 체킹을 위해 트랜잭션 사용
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
}
