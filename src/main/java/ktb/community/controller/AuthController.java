package ktb.community.controller;

import jakarta.validation.Valid;
import ktb.community.common.ApiResponse;
import ktb.community.dto.auth.request.LoginRequest;
import ktb.community.dto.auth.response.LoginResponse;
import ktb.community.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse data = authService.login(loginRequest);

        return ResponseEntity.ok(ApiResponse.success("로그인에 성공하였습니다.", data));
    }

}
