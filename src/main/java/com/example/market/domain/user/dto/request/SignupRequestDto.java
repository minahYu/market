package com.example.market.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[a-zA-Z0-9]{3,}$",
            message = "nickname은 최소 3자이고, 알파벳 대소문자와 숫자로 구성되어야 합니다.")
    private String nickname;

    @Size(min = 4, message = "password는 최소 4자 이상이어야 합니다.")
    private String password;

    private String passwordCheck;
}
