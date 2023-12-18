package com.example.market.global.security;

import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 정보를 제공하는 서비스 구현 클래스
 */
@Service
@RequiredArgsConstructor
// UserDetailsService : 사용자별 데이터를 로드하는 핵심 로드하는 핵심 인터페이스
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * 해당 닉네임의 유저를 DB에서 찾아 UserDetails에 리턴하는 메서드
     */
    @Override
    // UserDetails : 사용자 정보를 제공하는 인터페이스.
    //               스프링 시큐리티에서 직접 사용되지 않는 보안과 관련되지 않은 사용자 정보를 편리한 위치에 저장 가능.
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException(("Not Found" + nickname)));

        return new UserDetailsImpl(user);
    }
}
