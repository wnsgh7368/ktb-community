package ktb.community.dto.comment.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
        @NotBlank(message = "댓글 내용에는 공백이 들어갈 수 없습니다")
        String content
) {}
