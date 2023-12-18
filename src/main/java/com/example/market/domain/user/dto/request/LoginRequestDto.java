package com.example.market.domain.user.dto.request;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String nickname;
    private String password;
}
