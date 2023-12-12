package com.example.market.domain.user.service;

import com.example.market.domain.user.dto.request.SignupRequestDto;
import com.example.market.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signup(SignupRequestDto requestDto) {

    }
}
