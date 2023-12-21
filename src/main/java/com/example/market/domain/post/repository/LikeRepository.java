package com.example.market.domain.post.repository;

import com.example.market.domain.post.entity.Like;
import com.example.market.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long id, Long userId);
}
