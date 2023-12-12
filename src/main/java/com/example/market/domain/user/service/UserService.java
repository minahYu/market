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
            User newUser = new User(requestDto.getUsername(), requestDto.getPassword());
            userRepository.save(newUser);
        }
    }
}
