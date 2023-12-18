package com.example.market.domain.post.controller;

import com.example.market.domain.post.dto.response.PreviewPostResponseDto;
import com.example.market.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public List<PreviewPostResponseDto> getPosts() {
        return postService.getPosts();
    }
}
