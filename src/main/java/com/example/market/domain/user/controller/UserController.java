package com.example.market.domain.user.controller;

import com.example.market.domain.user.dto.request.SignupRequestDto;
import com.example.market.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        boolean result = userService.signup(requestDto);
        
        if(result) {
            return ResponseEntity.ok().body("회원가입이 완료되었습니다");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 닉네임입니다.");
        }
    }
}
