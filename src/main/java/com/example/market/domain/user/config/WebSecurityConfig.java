package com.example.market.domain.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 설정을 담당하는 클래스
 */
@EnableWebSecurity // : Spring Security를 기본 설정을 활성화하기 위한 annotation.
public class WebSecurityConfig {

    @Bean // :외부 라이브러리를 Spring이 관리하도록 등록해주는 annotation
    // PasswordEncoder : 스프링 시큐리티에서 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 단방향 암호화를 지원하는 인터페이스.
    public PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder() : BCrypt해싱함수를 이용하는 PasswordEncoder로, 암호화를 해준다.
        //(BCrypt - 높은 보안성을 제공하며, 해시에 솔트(salt)를 추가하는 기능이 내장되어 있어 안전)
        return new BCryptPasswordEncoder();
    }
}
