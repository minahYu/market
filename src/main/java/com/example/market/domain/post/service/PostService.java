package com.example.market.domain.post.service;

import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.PostResponseDto;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.PostRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
import com.example.market.global.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
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
     * 게시글 전체 목록을 조회하는 메서드
     */
    public List<PostResponseDto> getPosts() {
        List<Post> sortedPostList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PostResponseDto> postList = new ArrayList<>();

        sortedPostList.forEach(post -> postList.add(new PostResponseDto(post)));

        return postList;
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

    /**
     * 게시글 수정 관련 메서드
     */
    @Transactional
    public void updatePost(PostRequestDto requestDto, Long id, User user) {
        Optional<Post> postCheck = postRepository.findById(id);
        Post post = postCheck.get();

        if(postCheck.isPresent()) {
            validateWriter(post, user);
            post.update(requestDto.getTitle(), requestDto.getContents());
        } else {
            throw new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.");
        }
    }

    /**
     * 수정, 삭제를 진행하는 사람이 작성자가 맞는지 확인하는 메서드
     */
    private void validateWriter(Post post, User user) {
        if(!post.getWriter().equals(user.getNickname()))
            throw new IllegalArgumentException("다른 사람의 게시글은 수정 및 삭제가 불가능합니다.");
    }
}
