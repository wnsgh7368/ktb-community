package ktb.community.controller;

import ktb.community.common.ApiResponse;
import ktb.community.dto.post.request.CreatePostRequest;
import ktb.community.dto.post.request.UpdatePostRequest;
import ktb.community.dto.post.response.CreatePostResponse;
import ktb.community.dto.post.response.GetPostDetailResponse;
import ktb.community.dto.post.response.GetPostsResponse;
import ktb.community.dto.post.response.UpdatePostResponse;
import ktb.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    // 게시글 생성 API 컨트롤러
    @PostMapping
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @AuthenticationPrincipal Long userId,
            @RequestBody CreatePostRequest createPostRequest) {

        // 서비스 코드 호출 후 응답 DTO 생성
        CreatePostResponse createPostResponse = postService.createPost(userId, createPostRequest);

        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 등록되었습니다.", createPostResponse));
    }

    //TODO: 게시글 전체 조회 API (페이지 네이션 포함) 구현
    @GetMapping
    public ResponseEntity<ApiResponse<GetPostsResponse>> getPosts(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size ) {

        GetPostsResponse getPostsResponse = postService.getPosts(cursor, size);

        return ResponseEntity.ok(ApiResponse.success("성공적으로 조회되었습니다.", getPostsResponse));
    }

    // 게시글 상세 조회 API 컨트롤러
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<GetPostDetailResponse>> getPostDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId) {

        // 서비스 코드 호출 후 응답 DTO 생성
        GetPostDetailResponse getPostDetailResponse = postService.getPostDetail(userId, postId);

        return ResponseEntity.ok(ApiResponse.success("게시글을 성공적으로 조회하였습니다", getPostDetailResponse));
    }

    // 게시글 수정 API 컨트롤러
    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest updatePostRequest) {

        // 서비스 코드 호출 후 응답 DTO 생성
        UpdatePostResponse updatePostResponse = postService.updatePost(userId, postId, updatePostRequest);

        return ResponseEntity.ok(ApiResponse.success("게시글을 성공적으로 수정하였습니다", updatePostResponse));
    }

    // 게시글 삭제 API 컨트롤러
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId) {

        postService.deletePost(userId, postId);

        return ResponseEntity.ok(ApiResponse.success("게시글을 성공적으로 삭제하였습니다."));
    }
}
