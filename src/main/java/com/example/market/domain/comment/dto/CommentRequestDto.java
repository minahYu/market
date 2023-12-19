package com.example.market.domain.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @Size(min=10, max=5000)
    private String contents;
}
