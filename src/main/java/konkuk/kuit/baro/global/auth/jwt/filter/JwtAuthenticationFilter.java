package konkuk.kuit.baro.global.auth.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import konkuk.kuit.baro.global.auth.jwt.service.JwtService;
import konkuk.kuit.baro.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT Filter Request URI: {}", request.getRequestURI());

        Optional<String> optionalAccessToken = jwtUtil.extractAccessToken(request);

        // 토큰이 없으면 그냥 통과
        if (optionalAccessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = optionalAccessToken.get();

        // 토큰 검증
        jwtUtil.validateToken(accessToken);
        jwtService.checkBlacklistedToken(accessToken);

        // SecurityContext에 인증 정보 저장
        Long userId = jwtUtil.getUserId(accessToken);
        log.info("Authenticated userId: {}", userId);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
