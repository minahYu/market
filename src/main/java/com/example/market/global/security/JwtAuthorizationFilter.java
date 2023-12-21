package com.example.market.global.security;

import com.example.market.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 검증 및 인가를 담당하는 클래스
 */
@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
// OncePerRequestFilter : 모든 서블릿 컨테이너에서 요청 디스패치당 단일 실행을 목표로하는 필터 클래스.
//                        doFilterInternal 메서드 제공.
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    // doFilterInternal : 각 요청(동일한 요청)에 대해 한 번만 호출되는 메서드
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        String url = request.getRequestURI();

        log.info("@@URL : " + url);

        if((url.startsWith("/api/users") || (request.getMethod().equals("GET") && url.startsWith("/api/posts"))
                || url.startsWith("/css") || url.startsWith("/js"))) {
            filterChain.doFilter(request, response);
        } else {
            if (StringUtils.hasText(tokenValue)) {
                log.info("Token : " + tokenValue);
                tokenValue = jwtUtil.substringToken(tokenValue);
                log.info(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("Token Error");
                    return;
                }

                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }
            filterChain.doFilter(request, response); // 다음 필터로 이동
        }



    }

    /**
     * 인증처리하는 메서드
     */
    public void setAuthentication(String nickname) {
        // SecurityContext : 현재 작업 중인 스레드와 관련된 보안 정보를 담는 인터페이스로, 해당 정보는 SecurityContextHolder에 저장됨.
        // SecurityContextHolder : 현재 작업 중인 스레드에 연결된 SecurityContext를 저장하고 관리하는 클래스
        // createEmptyContext() : 비어있는 컨텍스트를 생성하는 메서드
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(nickname);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    /**
     * 인증 객체 생성
     */
    private Authentication createAuthentication(String nickname) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
