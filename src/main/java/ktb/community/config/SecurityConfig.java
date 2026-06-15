package ktb.community.config;

import ktb.community.securiy.JWTAuthenticationFilter;
import ktb.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private static final String[] PUBLIC_PATH = {
            // auth 관련
            "/auth/**",
            "/users"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> origins = new java.util.ArrayList<>();
        origins.add("http://localhost:3000");
        origins.add("https://talk2wall.com");
        origins.add("https://www.talk2wall.com");
        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .cors(cors -> {})
                // CsrfFilter 끄기
                .csrf(CsrfConfigurer::disable)
                // UserPasswordAuthenticationFilter 끄기
                .formLogin(FormLoginConfigurer::disable)
                // Session 관련 Filter 끄기
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // BasicAuthenticationFilter 끄기
                .httpBasic(HttpBasicConfigurer::disable)
                // JWT 필터 추가
                .addFilterBefore(new JWTAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // public api와 private api 구분
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_PATH).permitAll()
                        .anyRequest().authenticated());

        return http.build();

//        SecurityFilterChain chain = http.build();
//
////        // 필터 목록 출력
////        chain.getFilters().forEach(filter ->
////                System.out.println("필터: " + filter.getClass().getSimpleName())
////        );
    }
}
