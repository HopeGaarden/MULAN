package com.example.back.domain.auth.member;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.constant.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @Builder
    public Member(String nickname, String email, MemberRole memberRole) {
        this.nickname = nickname;
        this.email = email;
        this.memberRole = memberRole;
    }
}