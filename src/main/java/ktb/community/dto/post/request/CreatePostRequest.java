package ktb.community.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank(message = "제목에는 공백이 들어갈 수 없습니다.")
        @Size(max = 26, message = "제목은 최대 26글자입니다.")
        String title,

        @NotBlank(message = "내용에는 공백이 들어갈 수 없습니다")
        String content,

        String postImageUrl
) {}
