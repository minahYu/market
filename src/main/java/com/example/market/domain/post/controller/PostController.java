package com.example.market.domain.post.controller;

import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.PostResponseDto;
import com.example.market.domain.post.service.PostService;
import com.example.market.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @PostMapping("")
    public void createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.createPost(requestDto, userDetails.getUser());
    }
}
