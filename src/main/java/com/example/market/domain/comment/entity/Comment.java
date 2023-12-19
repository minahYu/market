package com.example.market.domain.comment.entity;

import com.example.market.domain.comment.dto.CommentRequestDto;
import com.example.market.domain.model.BaseEntity;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.contents = requestDto.getContents();
        this.post = post;
        this.user = user;
    }
}
