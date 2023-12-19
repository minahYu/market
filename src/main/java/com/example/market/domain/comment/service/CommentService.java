package com.example.market.domain.comment.service;

import com.example.market.domain.comment.dto.CommentRequestDto;
import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.comment.repository.CommentRepository;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.PostRepository;
import com.example.market.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void createComment( CommentRequestDto requestDto, Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Comment comment = new Comment(requestDto, post, user);
        commentRepository.save(comment);
    }
}
