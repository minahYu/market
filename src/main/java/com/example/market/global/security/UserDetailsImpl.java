package com.example.market.global.security;

import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 사용자 인증 정보를 관리하는 UserDetails 구현 클래스
 */
@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final User user;

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 사용자에게 부여된 권한을 반환하는 UserDetails 인터페이스의 메서드
     * 오버라이드하여 인증 객체에 부여된 권한을 가져와 GrantedAuthority 컬렉션으로 만들어 반환해줌.
     */
    @Override
    // GrantedAuthority : 인증 개체에 부여된 권한을 나타내는 인터페이스
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();
        // SimpleGrantedAuthority : 권한을 문자열 형태로 저장하는 클래스
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    /**
     * 사용자의 계정이 만료되었는지 여부를 리턴하는 메서드
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 사용자가 잠겨있는지를 나타하고 여부를 리턴하는 메서드
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 사용자의 비밀번호가 만료되었는지 여부를 반환하는 메서드
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 계정이 활성화 상태인지 여부를 반환하는 메서드
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
