package com.example.market.global.config;

import com.example.market.global.jwt.JwtUtil;
import com.example.market.global.security.JwtAuthenticationFilter;
import com.example.market.global.security.JwtAuthorizationFilter;
import com.example.market.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 담당하는 클래스
 */
@EnableWebSecurity // : Spring Security를 기본 설정을 활성화하기 위한 annotation.
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    // AuthenticationConfiguration : 인증 구성을 내보내는 클래스
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean // :외부 라이브러리를 Spring이 관리하도록 등록해주는 annotation
    // PasswordEncoder : 스프링 시큐리티에서 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 단방향 암호화를 지원하는 인터페이스.
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder() : BCrypt해싱함수를 이용하는 PasswordEncoder로, 암호화를 해준다.
        //(BCrypt - 높은 보안성을 제공하며, 해시에 솔트(salt)를 추가하는 기능이 내장되어 있어 안전)
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 Bean으로 등록하고 반환하는 메서드
     */
    @Bean
    // AuthenticationManager : 인증 요청을 처리하는 인터페이스로, authenticate 메서드를 가짐.
    public AuthenticationManager authenticationManager(
            // AuthenticationConfiguration : 인증 구성을 내보내는 클래스
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager(); // configuration에서 AuthenticationManager를 가져와 리턴
    }

    /**
     * 인증 필터 등록
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /**
     * 인가 필터 등록
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }


    /**
     * HTTP 요청에 대한 보안 설정을 하는 메서드
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 설정
        // => REST API는 stateless하게 동작해야하기 때문에 CSRF 공격이 불가능하므로 비활성화
        // (CSRF : 공격자가 사용자의 세션 정보를 활용하여 사용자가 의도하지 않은 행동을 수행하게 하는 공격)
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        // sessionManagement : 세션을 관리하는 메서드
        // SessionCreationPolicy : 세션 생성 정책 설정하는 메서드
        http.sessionManagement((sessionManagement) ->
                // STATELESS -> 스프링 시큐리티가 세션을 생성하지 않고 기존의 세션을 사용하지 않게끔 설정.
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 인가 요청
        http.authorizeHttpRequests((authorizationRequest) ->
                authorizationRequest
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인페이지 요청 허가
                        .requestMatchers("/api/users/**").permitAll() // 해당 경로로 시작하는 요청 모두 접근 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/api/users/login-page").permitAll()
        );

        // 필터 관리. 첫번째 인자 실행 전에 두번째 인자가 실행됨.
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // HttpSecurity 객체에 설정된 보안 설정을 적용하고, 설정된 SecurityFilterChain 객체를 반환
        return http.build();
    }
}
