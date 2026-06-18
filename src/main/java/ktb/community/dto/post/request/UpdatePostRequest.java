package ktb.community.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(
        @NotBlank(message = "제목에는 공백이 들어갈 수 없습니다.")
        @Size(max = 26)
        String title,

        @NotBlank(message = "내용에는 공백이 들어갈 수 없습니다")
        String content,

        String postImageUrl
) {}
