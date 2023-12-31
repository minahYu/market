package com.example.market.domain.post.service;

import com.example.market.domain.comment.dto.response.CommentResponseDto;
import com.example.market.domain.comment.entity.Comment;
import com.example.market.domain.comment.repository.CommentRepository;
import com.example.market.domain.post.dto.request.PostRequestDto;
import com.example.market.domain.post.dto.response.DetailPostResponseDto;
import com.example.market.domain.post.dto.response.LikeResponseDto;
import com.example.market.domain.post.dto.response.PreviewPostResponseDto;
import com.example.market.domain.post.entity.Like;
import com.example.market.domain.post.entity.Post;
import com.example.market.domain.post.repository.LikeRepository;
import com.example.market.domain.post.repository.PostRepository;
import com.example.market.domain.user.entity.User;
import com.example.market.domain.user.repository.UserRepository;
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
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    /**
     * 입력받은 게시글 데이터를 저장하는 메서드
     */
    public PreviewPostResponseDto createPost(PostRequestDto requestDto, User user) {
        User existUser = userRepository.findByNickname(user.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Post post = new Post(requestDto, user);
        postRepository.save(post);

        return new PreviewPostResponseDto(post);
    }

    /**
     * 게시글 전체 목록을 조회하는 메서드
     */
    public List<PreviewPostResponseDto> getPosts() {
        List<Post> sortedPostList = postRepository.findAllByOrderByCreatedAtDesc();
        List<PreviewPostResponseDto> postList = new ArrayList<>();
        sortedPostList.forEach(post -> postList.add(new PreviewPostResponseDto(post)));

        return postList;
    }

    /**
     * id에 해당하는 게시글을 조회하는 메서드
     */
    public DetailPostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        List<CommentResponseDto> commentList = getCommentList(id);
        List<LikeResponseDto> likeList = getLikeList(id);

        return new DetailPostResponseDto(post, commentList, likeList);
    }

    /**
     * 댓글 목록을 보여주기 위해 댓글들을 가져오는 메서드
     */
    private List<CommentResponseDto> getCommentList(Long id) {
        List<Comment> comments = commentRepository.findAllByPostId(id);
        List<CommentResponseDto> commentList = new ArrayList<>();
        comments.forEach(comment -> {
            commentList.add(new CommentResponseDto(comment));
        });

        return commentList;
    }

    /**
     * 좋아요 목록을 보여주기 위해 좋아요 누른 사람들을 가져오는 메서드
     */
    private List<LikeResponseDto> getLikeList(Long id) {
        List<Like> likes = likeRepository.findAllByPostId(id);
        List<LikeResponseDto> likeList = new ArrayList<>();
        likes.forEach(like -> {
            likeList.add(new LikeResponseDto(like.getUser()));
        });

        return likeList;
    }

    /**
     * 게시글 수정 관련 메서드
     */
    @Transactional
    public void updatePost(PostRequestDto requestDto, Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        validateWriter(post, user);
        post.update(requestDto.getTitle(), requestDto.getContents());
    }

    /**
     * 게시글 삭제 관련 메서드
     */
    @Transactional
    public boolean deletePost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다."));
        validateWriter(post, user);
        postRepository.delete(post);
        return true;
    }

    /**
     * 수정, 삭제를 진행하는 사람이 작성자가 맞는지 확인하는 메서드
     */
    private void validateWriter(Post post, User user) {
        if (!post.getUser().getNickname().equals(user.getNickname()))
            throw new IllegalArgumentException("다른 사람의 게시글은 수정 및 삭제가 불가능합니다.");
    }

    /**
     * 좋아요 관련 메서드 (ex; emptyHeart -> fullHeart)
     */
    public void addLikePost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다."));
        likeRepository.save(new Like(user, post));
    }

    /**
     * 좋아요 취소 메서드 (ex; fullHeart -> emptyHeart)
     */
    public void deleteLikePost(Long id, User user) {
        Like like = likeRepository.findByPostIdAndUserId(id, user.getId())
                .orElseThrow(()
                        -> new IllegalArgumentException("존재하지 않는 게시물이거나 해당 게시물에 좋아요를 누르지 않았습니다."));
        likeRepository.delete(like);
    }
}
