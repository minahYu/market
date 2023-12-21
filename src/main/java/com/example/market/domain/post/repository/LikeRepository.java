package com.example.market.domain.post.repository;

import com.example.market.domain.post.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
