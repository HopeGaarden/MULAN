package com.example.back.domain.group.member;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.group.groupInfo.GroupInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GroupMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_INFO_ID", nullable = false)
    private GroupInfo groupInfo;                 // 그룹 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;                      // 사용자 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "GROUP_MEMBER_STATUS", nullable = false)
    @ColumnDefault(value = "'MEMBER'")
    private GroupMemberStatus status;           // 그룹 멤버 상태

    @Builder
    public GroupMember(GroupInfo groupInfo, Member member, GroupMemberStatus status) {
        this.groupInfo = groupInfo;
        this.member = member;
        this.status = status;
    }
}
