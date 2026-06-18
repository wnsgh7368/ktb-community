package ktb.community.controller;

import ktb.community.common.ApiResponse;
import ktb.community.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}")
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요 등록 API 컨트롤러
    @PostMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> createLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId) {

        likeService.createLike(userId, postId);

        return ResponseEntity.ok(ApiResponse.success("좋아요를 성공적으로 등록하였습니다."));
    }

    // 게시글 좋아요 삭제 API 컨트롤러
    @DeleteMapping("/likes")
    public ResponseEntity<ApiResponse<Void>> deleteLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId ) {

        likeService.deleteLike(userId, postId);

        return ResponseEntity.ok(ApiResponse.success("좋아요를 성공적으로 취소하였습니다."));
    }
}
