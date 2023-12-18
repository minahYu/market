package com.example.market.domain.post.service;

import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.PostResponseDto;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.PostRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 전체 목록을 조회하는 메서드
     */
    public List<PostResponseDto> getPosts() {
        List<Post> sortedPostList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postList = new ArrayList<>();

        sortedPostList.forEach(post -> postList.add(new PostResponseDto(post)));

        return postList;
    }

    /**
     * 입력받은 게시글 데이터를 저장하는 메서드
     */
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Optional<User> userCheck = userRepository.findByNickname(user.getNickname());

        if(userCheck.isPresent()) {
            Post post = new Post(requestDto, user.getNickname());
            postRepository.save(post);

            return new PostResponseDto(post);
        }
        else {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
    }

    /**
     * id에 해당하는 게시글을 조회하는 메서드
     */
    public PostResponseDto getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()) {
            return new PostResponseDto(post.get());
        } else {
            throw new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.");
        }
    }
}
