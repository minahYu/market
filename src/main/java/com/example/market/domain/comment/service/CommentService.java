package com.example.market.domain.comment.service;

import com.example.market.domain.comment.dto.CommentRequestDto;
import com.example.market.domain.comment.dto.response.CommentResponseDto;
import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.comment.repository.CommentRepository;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.PostRepository;
import com.example.market.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto, Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto, post, user);
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        validateWriter(comment, user);
        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public Long deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        validateWriter(comment, user);
        commentRepository.delete(comment);

        return commentId;
    }

    private void validateWriter(Comment comment, User user) {
        if(!comment.getUser().getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("다른 사람의 댓글은 수정 및 삭제가 불가능합니다.");
        }
    }
}
