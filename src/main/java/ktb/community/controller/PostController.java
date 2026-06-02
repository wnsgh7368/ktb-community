package ktb.community.controller;

import ktb.community.common.ApiResponse;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.dto.post.response.GetPostDetailResponse;
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

    // 게시글 생성 API 컨트롤러
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody CreatePostRequest createUserRequest) {

        // 헤더에서 토큰 추출 및 토큰에서 userId 추출
        String token = jwtUtil.extractToken(authorization);
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 서비스 코드 호출 후 응답 DTO 생성
        CreatePostResponse createPostResponse = postService.createPost(userId, createUserRequest);

        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 등록되었습니다.", createPostResponse));
    }
    //TODO: 게시글 전체 조회 API (페이지 네이션 포함) 구현

    // 게시글 상세 조회 API 컨트롤러
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<GetPostDetailResponse>> getPostDetail(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long postId) {

        // 헤더에서 토큰 추출 및 토큰에서 userId 추출
        String token = jwtUtil.extractToken(authorization);
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 서비스 코드 호출 후 응답 DTO 생성
        GetPostDetailResponse getPostDetailResponse = postService.getPostDetail(userId, postId);

        return ResponseEntity.ok(ApiResponse.success("게시글을 성공적으로 조회하였습니다", getPostDetailResponse));
    }
}
