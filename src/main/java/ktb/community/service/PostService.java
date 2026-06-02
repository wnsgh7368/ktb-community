package ktb.community.service;

import ktb.community.common.ApiResponse;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.dto.user.request.CreateUserRequest;
import ktb.community.entity.Post;
import ktb.community.entity.User;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.repository.PostRepository;
import ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CreatePostResponse createPost(Long userId, CreatePostRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(req.title())
                .content(req.content())
                .postImageUrl(req.postImageUrl())
                .build();

        Post saved = postRepository.save(post);

        return new CreatePostResponse(saved.getId());
    }
}
