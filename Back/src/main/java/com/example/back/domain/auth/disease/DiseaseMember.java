package com.example.back.domain.auth.disease;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DiseaseMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISEASE_MEMBER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISEASE_INFO_ID", nullable = false)
    private DiseaseInfo diseaseInfo;             // 질병 그룹 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;                      // 사용자 정보

    @Enumerated(EnumType.STRING)
    @Column(name = "DISEASE_MEMBER_STATUS")
    @ColumnDefault(value = "'MEMBER'")
    private DiseaseMemberStatus status;         // 질병 그룹 멤버 상태

    @Builder
    public DiseaseMember(DiseaseInfo diseaseInfo, Member member, DiseaseMemberStatus status) {
        this.diseaseInfo = diseaseInfo;
        this.member = member;
        this.status = status;
    }
}
