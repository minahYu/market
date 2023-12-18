package com.example.market.domain.post.dto.response;

import com.example.market.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DetailPostResponseDto {
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime createdAt;

    public DetailPostResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.writer = post.getWriter();
        this.createdAt = post.getCreatedAt();
    }
}
