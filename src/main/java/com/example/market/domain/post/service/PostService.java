package com.example.market.domain.post.service;

import com.example.market.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    /**
     * 게시물 전체 목록을 조회하는 메서드
     */
    public void getPosts() {

    }
}
