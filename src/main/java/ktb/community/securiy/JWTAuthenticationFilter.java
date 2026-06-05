package ktb.community.securiy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ktb.community.exception.CustomException;
import ktb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. header에서 Authorization 꺼내기
        String authorization = request.getHeader("Authorization");

        // 2. 토큰 비어있으면 다음 필터로 넘기기
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. authorization에서 토큰만 추출
            String token = jwtUtil.extractToken(authorization);
            // 4. 토큰 검증, 실패하면 메서드 내부에서 CustomException을 던짐
            jwtUtil.validateToken(token);
            // 5. userId 꺼내기
            Long userId = jwtUtil.getUserId(token);
            // 6. Security Context에 userId 넣어놓기
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
            // 7. 다음 필터로 이동
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            throw new RuntimeException(e); //TODO: 검증 실패 했을 때, 예외 처리 구현
        }

    }
}
