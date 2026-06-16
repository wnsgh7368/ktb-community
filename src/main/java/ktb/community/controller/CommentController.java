package ktb.community.controller;

import jakarta.validation.Valid;
import ktb.community.common.ApiResponse;
import ktb.community.dto.comment.request.CreateCommentRequest;
import ktb.community.dto.comment.request.UpdateCommentRequest;
import ktb.community.dto.comment.response.CreateCommentResponse;
import ktb.community.dto.comment.response.GetCommentsResponse;
import ktb.community.dto.comment.response.UpdateCommentResponse;
import ktb.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody @Valid CreateCommentRequest createCommentRequest) {

        CreateCommentResponse createCommentResponse = commentService.createComment(userId, postId, createCommentRequest);

        return ResponseEntity.ok(ApiResponse.success("댓글이 성공적으로 생성되었습니다", createCommentResponse));
    }
    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<UpdateCommentResponse>> updateComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest ) {

        UpdateCommentResponse updateCommentResponse = commentService.updateComment(userId, postId, commentId, updateCommentRequest);

        return ResponseEntity.ok(ApiResponse.success("댓글 수정에 성공하였습니다", updateCommentResponse));
    }
    // 댓글 조회(인피니티 스크롤링 구현)
    @GetMapping
    public ResponseEntity<ApiResponse<GetCommentsResponse>> getComments(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size ) {

        GetCommentsResponse getCommentsResponse = commentService.getComments(userId, postId, cursor, size);

        return ResponseEntity.ok(ApiResponse.success("댓글 조회가 성공적으로 완료되었습니다", getCommentsResponse));
    }
    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId ) {

        commentService.deleteComment(userId, postId, commentId);

        return ResponseEntity.ok(ApiResponse.success("성공적으로 댓글을 삭제하였습니다."));
    }

}
