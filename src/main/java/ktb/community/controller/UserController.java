package ktb.community.controller;

import ktb.community.common.ApiResponse;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.dto.user.request.CreateUserRequest;
import ktb.community.dto.user.request.UpdatePasswordRequest;
import ktb.community.dto.user.request.UpdateProfileRequest;
import ktb.community.dto.user.response.UserIdResponse;
import ktb.community.service.UserService;
import ktb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping
    public ResponseEntity<ApiResponse<UserIdResponse>> createUser(
            @RequestBody CreateUserRequest createUserRequest
    ) {
        UserIdResponse data = userService.createUser(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입에 성공하였습니다.", data));
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<UserIdResponse>> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {

        UserIdResponse data = userService.updateProfile(userId, updateProfileRequest);
        return ResponseEntity.ok(ApiResponse.success("유저 정보 수정에 성공하였습니다.", data));
    }

    // 3. 비밀번호 수정
    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @AuthenticationPrincipal Long userId,
            @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        userService.updatePassword(userId, updatePasswordRequest);

        return ResponseEntity.ok(ApiResponse.success("비밀번호 수정에 성공하였습니다."));
    }

    // 4. 이메일 중복 체크
    @GetMapping(params = "email")
    public ResponseEntity<ApiResponse<Void>> checkDuplicateEmail(@RequestParam(required = false) String email) {

        if (email != null) {
            userService.checkEmailAvailable(email);
            return ResponseEntity.ok(ApiResponse.success("사용할 수 있는 이메일입니다."));
        }
        throw new CustomException(ErrorCode.INVALID_REQUEST);
    }
    // 5. 닉네임 중복 체크
    @GetMapping(params = "nickname")
    public ResponseEntity<ApiResponse<Void>> checkDuplicateNickname(
            @RequestParam(required = false) String nickname
    ) {
        if (nickname != null) {
            userService.checkNicknameAvailable(nickname);
            return ResponseEntity.ok(ApiResponse.success("사용할 수 있는 닉네임입니다."));
        }
        throw new CustomException(ErrorCode.INVALID_REQUEST);
    }


}
