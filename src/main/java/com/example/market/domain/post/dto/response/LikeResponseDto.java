package com.example.market.domain.post.dto.response;

import com.example.market.domain.user.entity.User;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private String nickname;

    public LikeResponseDto(User user) {
        this.nickname = user.getNickname();
    }
}
