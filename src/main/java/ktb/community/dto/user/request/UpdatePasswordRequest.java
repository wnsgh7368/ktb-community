package ktb.community.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
                message = "비밀번호는 8자 이상 20자 이하이며, 대문자/소문자/숫자/특수문자(@$!%*?&)를 각각 최소 1개 포함해야 합니다.")
        @NotBlank(message = "비밀번호 값이 잘못되었습니다.")
        String password
) {}
