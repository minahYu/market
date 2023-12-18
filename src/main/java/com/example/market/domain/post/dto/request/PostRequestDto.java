package com.example.market.domain.post.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequestDto {
    @Size(max = 500)
    private String title;

    @Size(max = 5000)
    private String contents;
}
