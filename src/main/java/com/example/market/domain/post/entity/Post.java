package com.example.market.domain.post.entity;

import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.model.BaseEntity;
import com.example.market.domain.post.dto.request.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "writer", nullable = false)
    private String writer;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Like> likeList = new ArrayList<>();

    public Post(PostRequestDto requestDto, String writer) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.writer = writer;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
