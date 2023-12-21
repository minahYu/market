package com.example.market.domain.post.controller;

import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.DetailPostResponseDto;
import com.example.market.domain.post.dto.response.PreviewPostResponseDto;
import com.example.market.domain.post.service.PostService;
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

    /**
     * 게시글 생성관련 메서드
     */
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

    /**
     * 게시글 전체 목록 조회 관련 메서드
     */
    @GetMapping("")
    public List<PreviewPostResponseDto> getPosts() {
        return postService.getPosts();
    }

    /**
     * 게시글 단일 조회 관련 메서드
     */
    @GetMapping("/{id}")
    public DetailPostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }


    /**
     * 게시글 수정관련 메서드
     */
    @PostMapping("/{id}")
    public void updatePost(
            @RequestBody PostRequestDto requestDto,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        postService.updatePost(requestDto, id, userDetails.getUser());
    }

    /**
     * 게시글 삭제 관련 메서드
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boolean result = postService.deletePost(id, userDetails.getUser());

        if(!result) {
            return ResponseEntity.status(401).body("게시글을 삭제하지 못했습니다.");
        }
        return ResponseEntity.status(200).body("게시글이 삭제되었습니다.");
    }

    /**
     * 좋아요 관련 메서드 (ex; emptyHeart -> fullHeart)
     */
    @PostMapping("/{id}/like")
    public void addLikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {

    }
}
