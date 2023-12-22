package com.example.market.domain.comment.controller;

import com.example.market.domain.comment.dto.request.CommentRequestDto;
import com.example.market.domain.comment.dto.response.CommentResponseDto;
import com.example.market.domain.comment.service.CommentService;
import com.example.market.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(
            @RequestBody CommentRequestDto requestDto,
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto responseDto = commentService.createComment(requestDto, postId, userDetails.getUser());
        if(responseDto != null) {
            return ResponseEntity.status(200).body("댓글을 등록하였습니다.");
        }
        return ResponseEntity.status(401).body("댓글을 등록할 수 없습니다.");
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
        @RequestBody CommentRequestDto requestDto,
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        CommentResponseDto responseDto = commentService.updateComment(requestDto, commentId, userDetails.getUser());

        if(responseDto != null) {
            return ResponseEntity.status(200).body("댓글을 수정하였습니다.");
        }
        return ResponseEntity.status(401).body("댓글을 수정할 수 없습니다.");
    }

    /**
     * 댓글 삭제 관련 메서드
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long id = commentService.deleteComment(commentId, userDetails.getUser());

        if(id != null) {
            return ResponseEntity.status(200).body("댓글을 삭제하였습니다.");
        }
        return ResponseEntity.status(401).body("댓글을 삭제할 수 없습니다.");
    }
}
