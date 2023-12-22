package com.example.market.domain.post.dto.response;

import com.example.market.domain.post.entity.Post;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PreviewPostResponseDto {
    private String title;
    private String writer;
    private LocalDateTime createdAt;

    public PreviewPostResponseDto(Post post) {
        this.title = post.getTitle();
        this.writer = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
    }
}
