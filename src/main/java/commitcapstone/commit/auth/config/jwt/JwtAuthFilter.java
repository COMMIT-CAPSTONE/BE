package commitcapstone.commit.auth.config.jwt;



import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


public class JwtAuthFilter extends OncePerRequestFilter {


    private JwtTokenProvider jwtTokenProvider;
    public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 인증 제외 경로
        if (path.startsWith("/api/auth") || path.startsWith("/api/oauth") || path.startsWith("/oauth") || path.equals("/oauth/google/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("JwtAuthFilter start");
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String email = jwtTokenProvider.getUserEmail(token);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                logger.error("JWT 검증 실패: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid JWT token");
                return;
            }
        } else {

            logger.warn("Authorization 헤더 없음 또는 잘못된 형식");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authorization header missing or malformed");
            return;
        }

        filterChain.doFilter(request, response);
    }

}