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
        Optional<User> user = userRepository.findByUsername(requestDto.getUsername());

        if(user.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        } else {
            if(checkSamePassword(requestDto.getPassword(), requestDto.getPasswordCheck())) {
                User newUser = new User(requestDto.getUsername(), requestDto.getPassword());
                userRepository.save(newUser);
            }
        }
    }

    /**
     * password와 passwordCheck가 같은 값인지 확인하는 메서드
     */
    private boolean checkSamePassword(String password, String passwordCheck) {
        if(password.matches(passwordCheck)) {
            return true;
        } else {
            throw new IllegalArgumentException("입력하신 비밀번호와 일치하지 않습니다");
        }
    }
}
