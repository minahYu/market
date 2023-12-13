package com.example.market.domain.user.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "password")
    private String password;

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}
