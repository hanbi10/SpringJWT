package com.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users") // 테이블명 명시적 지정
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY == id가 겹치지 않고, 자동화로 잘 생성이 된다.
    private int id;

    @Column(unique = true, nullable = false) // username 중복 방지 + null 방지
    private String username;

    @Column(nullable = false) // 비밀번호 null 방지
    private String password;

    @Column(nullable = false) // 역할 null 방지
    private String role;
}