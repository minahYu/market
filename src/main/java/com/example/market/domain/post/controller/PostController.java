package com.example.market.domain.post.controller;

import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.PostResponseDto;
import com.example.market.domain.post.service.PostService;
import com.example.market.domain.user.entity.User;
import com.example.market.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<?> createPost(
            @RequestBody PostRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if(postService.createPost(requestDto, userDetails.getUser()) != null) {
            return ResponseEntity.status(200).body("게시글을 등록하였습니다.");
        }
        return ResponseEntity.status(401).body("게시글 등록을 실패하였습니다.");
    }

    @GetMapping("")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PostMapping("/{id}")
    public void updatePost(
            @RequestBody PostRequestDto requestDto,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.updatePost(requestDto, id, userDetails.getUser());
    }
}
