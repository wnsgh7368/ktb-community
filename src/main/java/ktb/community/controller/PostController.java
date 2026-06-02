package ktb.community.controller;

import ktb.community.common.ApiResponse;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.service.PostService;
import ktb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody CreatePostRequest createUserRequest) {

        String token = jwtUtil.extractToken(authorization);
        Long userId = jwtUtil.getUserIdFromToken(token);

        CreatePostResponse createPostResponse = postService.createPost(userId, createUserRequest);

        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 등록되었습니다.", createPostResponse));
    }
}
