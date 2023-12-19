package com.example.market.domain.comment.dto.response;

import com.example.market.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    public CommentResponseDto(Comment comment) {

    }
}
