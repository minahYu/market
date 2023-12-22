package com.example.market.domain.user.entity;

/**
 * 사용자 권한을 설정하는 열거형 클래스
 */
public enum UserRoleEnum {
    USER(Authority.USER); // 일반 사용자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
    }
}
