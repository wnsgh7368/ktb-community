package ktb.community.service;

import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import ktb.community.dto.user.request.CreateUserRequest;
import ktb.community.dto.user.request.UpdatePasswordRequest;
import ktb.community.dto.user.request.UpdateProfileRequest;
import ktb.community.dto.user.response.UserIdResponse;
import ktb.community.entity.User;
import ktb.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
     * 1. 회원가입 서비스 코드
     */
    public UserIdResponse createUser(CreateUserRequest req) {
        // 1-1. 이메일 중복 체크 (중복 -> DUPLICATE_EMAIL 에러)
        if (userRepository.existsByEmail(req.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        // 1-2. 닉네임 중복 체크 (중복 -> DUPLICATE_NICKNAME 에러)
        if (userRepository.existsByNickname(req.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        // 1-3. User 객체 생성
        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .nickname(req.nickname())
                .profileImageUrl(req.profileImageUrl())
                .build();
        // 1-4. user 저장
        User saved = userRepository.save(user);
        return new UserIdResponse(saved.getId());
    }

    /*
     * 2. 프로필 수정 서비스 코드
     */
    public UserIdResponse updateProfile(Long userId, UpdateProfileRequest req) {
        // 2-1. userId로 DB에서 user 찾기 (없으면 -> USER_NOT_FOUND 에러)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 2-2. user의 email과 중복 체크 && DB에 이미 존재하는 email이 있는지 체크 (중복 -> DUPLICATE_EMAIL 에러)
        if (req.email() != null && !req.email().equals(user.getEmail())
                && userRepository.existsByEmail(req.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        // 2-3. user의 nickname과 중복 체크 && DB에 이미 존재하는 nickname이 있는지 체크 (중복 -> DUPLICATE_NICKNAME 에러)
        if (req.nickname() != null && !req.nickname().equals(user.getNickname())
                && userRepository.existsByNickname(req.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        /* 2-4. 모든 유효성 검증 통과시 실행되는 로직,
         * req의 데이터를 user 객체에 업데이트
         */
        user.updateProfile(req.email(), req.nickname(), req.profileImageUrl());
        userRepository.save(user);
        return new UserIdResponse(user.getId());
    }

    /*
     * 3. 비밀번호 수정 서비스 코드
     */
    public void updatePassword(Long userId, UpdatePasswordRequest req) {
        // 3-1. userId로 DB에서 user 찾기 (없으면 -> USER_NOT_FOUND 에러)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 3-1. req의 password를 user 객체에 업데이트
        user.changePassword(passwordEncoder.encode(req.password()));
        userRepository.save(user);
    }

    /*
     * 4. 이메일 유효성 검증 서비스 코드
     */
    public void checkEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    /*
     * 5. 닉네임 유효성 검증 서비스 코드
     */
    public void checkNicknameAvailable(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
