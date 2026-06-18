package ktb.community.service;

import ktb.community.common.ApiResponse;
import ktb.community.dto.auth.request.LoginRequest;
import ktb.community.dto.auth.response.LoginResponse;
import ktb.community.entity.User;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.repository.UserRepository;
import ktb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest req) {
        // 이메일 검증 -> 실패시 LOGIN_FAILED
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        // 비밀번호 검증 -> 실패시 LOGIN_FAILED
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        String accessToken = jwtUtil.generateAccessToken(user);

        return LoginResponse.of(user, accessToken);
    }
}

