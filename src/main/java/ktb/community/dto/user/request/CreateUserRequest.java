package ktb.community.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @Email(message = "이메일 형식이 잘못되었습니다.")
        @NotBlank(message = "이메일 값이 누락되었거나, 공백이 포함되어 있습니다.")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
                message = "비밀번호는 8자 이상 20자 이하이며, 대문자/소문자/숫자/특수문자(@$!%*?&)를 각각 최소 1개 포함해야 합니다.")
        @NotBlank(message = "비밀번호 값이 잘못되었습니다.")
        String password,


        @Pattern(regexp = "\\S+", message = "닉네임에는 띄어쓰기가 들어갈 수 없습니다.")
        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(max = 10, message = "닉네임은 최대 10글자 입니다.")
        String nickname,

        String profileImageUrl
) {}
