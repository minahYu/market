package com.example.market.domain.post.entity;

import com.example.market.domain.model.BaseEntity;
import com.example.market.domain.post.dto.request.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String writer;

}
