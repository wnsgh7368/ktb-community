package ktb.community.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import ktb.community.entity.User;
import ktb.community.exception.CustomException;
import ktb.community.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-time}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // AccessToken 생성 메서드, 추후에 claim 확장을 위해 User를 매개변수로 받음
    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Authorization 헤더 값을 순수 토큰만 추출하기 위한 메서드
     * @param authorization: Authorization Header의 값 (ex: "Bearer ")
     * @return: 순수한 jwt 토큰 값
     * @throws: CustomException: authorization이 비어 있거나 Bearer 토큰이 아닐 때 예외 처리
     */
    public String extractToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        return authorization.substring(7);
    }

    /**
     * 토큰 검증 메서드
     * @param token: 검증할 토큰
     * @return: 유효하면 true, 아니면 false 반환
     * @throws: CustomException: getClaims에서 던진 JwtException을 잡아 커스텀 예외로 처리
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
    }

    /**
     * 토큰에서 userId 가져오는 메서드
     */
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * JwtUtil 내부에서 사용하는 메서드
     * Claim을 반환하여, validateToken()이나 getUserId()에서 재사용한다.
     * @param token: 순수한 토큰 값
     * @return: Claim 반환
     * @throws JwtException: 토큰 검증 중에 발생하는 Exception을 던진다. 이후 validateToken()에서 예외 처리
     */
    private Claims getClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}

