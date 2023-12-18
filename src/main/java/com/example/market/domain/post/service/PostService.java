package com.example.market.domain.post.service;

import com.example.market.domain.post.dto.response.PostResponseDto;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /**
     * 게시물 전체 목록을 조회하는 메서드
     */
    public List<PostResponseDto> getPosts() {
        List<Post> sortedPostList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postList = new ArrayList<>();

        sortedPostList.forEach(post -> postList.add(new PostResponseDto(post)));

        return postList;
    }
}
