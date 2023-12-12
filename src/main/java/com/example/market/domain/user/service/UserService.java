package com.example.market.domain.user.service;

import com.example.market.domain.user.dto.request.SignupRequestDto;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();
        Optional<User> user = userRepository.findByNickname(requestDto.getNickname());

        if (user.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        } else {
            if (checkSamePassword(password, passwordCheck) && checkPasswordNotContainsUsername(password, nickname)) {
                User newUser = new User(nickname, password);
                userRepository.save(newUser);
            }
        }
    }

    /**
     * password와 passwordCheck가 같은 값인지 확인하는 메서드
     */
    private boolean checkSamePassword(String password, String passwordCheck) {
        if (password.matches(passwordCheck)) {
            return true;
        } else {
            throw new IllegalArgumentException("입력하신 비밀번호와 일치하지 않습니다");
        }
    }

    /**
     * password에 username이 포함되어있지 않은지 확인하는 메서드
     */
    private boolean checkPasswordNotContainsUsername(String username, String password) {
        if (password.contains(username))
            return true;
        else
            throw new IllegalArgumentException("password에는 username과 같은 값이 포함될 수 없습니다.");
    }
}
