package com.example.market.global.security;

import com.example.market.domain.user.dto.request.LoginRequestDto;
import com.example.market.domain.user.entity.UserRoleEnum;
import com.example.market.global.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * 로그인 기능을 갖고, JWT를 생성하는 클래스
 */
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/users/login"); // 인자로 받은 URL이 인증이 필요한지 결정하는 메서드
    }

    /**
     * 로그인을 시도하는 메서드
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            // ObjectMapper() : JsonFactory를 생성하는 기본 생성자.
            // readValue() : 입력받은 JSON형식의 request를 Java 객체로 역직렬화해주는 메서드
            // getInputStream() : 바이너리 데이터(파일이나 JSON 데이터)를 바이트 단위의 데이터를 읽어들여 InputStream을 반환하는 메서드
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getNickname(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("로그인 실패 : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 인증 성공시 호출되는 메서드
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        log.info("로그인 성공");
        // 인증 결과를 가져옴
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(nickname, role); // 닉네임과 역할을 이용해 토큰 생성
        jwtUtil.addJwtToCookie(token, response); // 생성한 토큰을 응답 쿠키에 넣어줌.
    }

    /**
     * 인증 실패시 호출되는 메서드
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("닉네임 또는 패스워드를 확인해주세요.");
    }
}
