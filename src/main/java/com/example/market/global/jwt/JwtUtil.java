package com.example.market.global.jwt;

import com.example.market.domain.user.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    /**
     * JWT 토큰을 쿠키에 저장하는 메서드
     */
    public void addJwtToCookie(String token, HttpServletResponse response) {
        try {
            // token을 utf-8형식으로 URL 인코딩하여 +를 %20으로 대체해줌.
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // 쿠키 생성
            cookie.setPath("/"); // 쿠키를 반환할 경로 설정

            response.addCookie(cookie); // 응답 데이터에 쿠키 추가
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * JWT 토큰에서 토큰 식별자를 자르는 메서드
     */
    public String substringToken(String tokenValue) {
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }
    
    /**
     * 토큰 검증하는 메서드
     * 검증 완료시 true, 실패시 false를 리턴.
     */
    public boolean validateToken(String token) {
        try {
            // parserBuilder() : JwtParser(변경 불가능하고 스레드로 부터 안전)를 생성하도록 구성하는
            //                   새 JwtParserBuilder 인스턴스를 반환.
            // setSigningKey() : JWT 생성시 사용한 비밀키를 이용해 JWT를 검증하는 메서드로,
            //                   문자열이 Jws가 아닌 경우 사용되지 않고, JWT 헤더에 있는 토큰이 유효해야 함.
            // parseClaimsJwt() : JWS 문자열 구문을 분석해 Claims JWS 인스턴스를 반환.
            //                    분석하고 검증하여 그 결과에 따라 아래에 있는 예외처리들을 던져줌.
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰입니다.");
        }
        return false;
    }

    /**
     * 토큰에서 사용자 정보를 가져오는 메서드
     */
    public Claims getUserInfoFromToken(String token) {
        // getBody() : JwtBody 부분을 String or Claims 리턴해주는 메서드
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
