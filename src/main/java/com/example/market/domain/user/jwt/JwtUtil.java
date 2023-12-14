package com.example.market.domain.user.jwt;

import com.example.market.domain.user.entity.UserRoleEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j

/**
 * JWT관련 기능들을 가진 클래스
 * (Util 클래스 : 다른 객체에 의존하지 않고 하나의 모듈로서 존재하는 클래스.
 *               특정 기능을 가진 메서드들의 모음.)
 */
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 값의 KEY
    
    // TOKEN 식별자
    // Bearer : JWT or OAuth에 대한 토큰을 사용하는 표시
    public static final String BEARER_PREFIX = "Bearer "; 
    private final long TOKEN_TIME = 60 * 60 * 1000L; //토큰 만료시간 60분

    // @Value : 설정파일에 설정한 내용을 주입시켜주는 annotation
    // - 해당 annotation을 사용하면 민감한 정보들을 노출시키지 않고도 설정이 가능해 보안에 유리하고, 유지보수에 용이
    @Value("${jwt.secret.key}") // Base64 Encode한 SecretKey.
    private String secretKey;

    // 암호화 키를 나타내는 모든 클래스의 상위 인터페이스. 모든 key객체에 대해 공유되는 기능들을 정의하고 있음.
    private Key key;

    // SignatureAlgorithm : JWT 라이브러리에서 제공하는 enum 클래스로, JWT 토큰을 생성하고 검증할 때 사용하는 서명 알고리즘.
    // HS256 : HMAC SHA-256 알고리즘. 메시지 인증을 위한 알고리즘으로,
    //         메시지와 비밀 키를 결합하여 메시지가 변조되지 않았음을 확인하는데 사용
    // JWT 토큰 생성 및 검증에 HS256 암호화 알고리즘 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 객체 생성 후, 초기화를 위해 처음 한 번만 실행되는 메서드
     */
    @PostConstruct
    // : init()과 같이 처음 한 번만 실행되는 메서드를 정의할 때 붙여주는 annotation
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); // Base64로 encode된 secret key를 다시 decode
        // Keys : 키를 안전하게 생성하기 위한 유틸리티 클래스
        key = Keys.hmacShaKeyFor(bytes); // HMAC SHA 암호화 방법을 이용해 bytes(decode된 secret key)를 암호화하고 생성
    }

    /**
     * 토큰을 생성하는 메서드
     */
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date(); // 토큰을 생성한 date객체 생성
        
        return BEARER_PREFIX +
                // Jwts : JWT 인터페이스 인스턴스를 생성하는데 유용한 팩토리 클래스
                //        (팩토리 클래스 - 필요한 객체를 만들어 제공하는 역할을 함.)
                Jwts.builder() // builder() : JwtBuilder 인스턴스를 반환.
                        .setSubject(username) // 사용자 식별자 값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() - TOKEN_TIME)) // 만료시간
                        .setIssuedAt(date) // 토큰 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // 위에서 구성한 내용들을 문자열 형태로 압축해 반환
    }
}
