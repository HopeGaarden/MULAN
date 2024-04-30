package com.example.back.domain.auth.follower;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follower extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOLLOWER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member follower;      // 팔로워

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member followed;      // 팔로잉

    @Builder
    public Follower(Member follower, Member followed) {
        this.follower = follower;
        this.followed = followed;
    }
}
