package com.example.market.domain.user.service;

import com.example.market.domain.user.dto.request.SignupRequestDto;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.entity.UserRoleEnum;
import com.example.market.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signup(SignupRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();
        Optional<User> user = userRepository.findByNickname(requestDto.getNickname());

        if (user.isPresent()) {
            //throw new IllegalArgumentException("중복된 닉네임입니다.");
            return false;
        } else {
            if (checkSamePassword(password, passwordCheck) && checkPasswordNotContainsNickname(password, nickname)) {
                password = passwordEncoder.encode(password);
                User newUser = new User(nickname, password, UserRoleEnum.USER);
                userRepository.save(newUser);
            }
            return true;
        }
    }

    /**
     * password와 passwordCheck가 같은 값인지 확인하는 메서드
     */
    private boolean checkSamePassword(String password, String passwordCheck) {
        if (password.equals(passwordCheck)) {
            return true;
        } else {
            throw new IllegalArgumentException("입력하신 비밀번호와 일치하지 않습니다");
        }
    }

    /**
     * password에 nickname이 포함되어있지 않은지 확인하는 메서드
     */
    private boolean checkPasswordNotContainsNickname(String nickname, String password) {
        if (!password.contains(nickname))
            return true;
        else
            throw new IllegalArgumentException("password에는 nickname과 같은 값이 포함될 수 없습니다.");
    }
}
