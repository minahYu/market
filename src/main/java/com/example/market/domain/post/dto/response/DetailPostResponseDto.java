package com.example.market.domain.post.dto.response;

import com.example.market.domain.comment.dto.response.CommentResponseDto;
import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.post.entity.Like;
import com.example.market.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class DetailPostResponseDto {
    private String title;
    private String contents;
    private String writer;
    private LocalDateTime createdAt;

    private List<CommentResponseDto> commentList;
    private List<LikeResponseDto> likeList;

    public DetailPostResponseDto(
            Post post,
            List<CommentResponseDto> commentList,
            List<LikeResponseDto> likeList
    ) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.writer = post.getWriter();
        this.createdAt = post.getCreatedAt();
        this.commentList = commentList;
        this.likeList = likeList;
    }
}
