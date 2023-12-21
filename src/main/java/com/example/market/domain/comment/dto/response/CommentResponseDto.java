package com.example.market.domain.comment.dto.response;

import com.example.market.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String writer;
    private String contents;

    public CommentResponseDto(Comment comment) {
        this.writer = comment.getUser().getNickname();
        this.contents = comment.getContents();
    }
}
