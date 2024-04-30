package com.example.back.domain.group.groupInfo;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.medical.DiseaseInfo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GroupInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_INFO_ID")
    private Long id;

    @Embedded
    @Column(name = "DISEASE_INFO")
    private DiseaseInfo diseaseInfo;            // 질병 정보

    @Column(name = "SCORE")
    private int score = 0; // 그룹 점수

    @Enumerated(EnumType.STRING)
    @Column(name = "GROUP_STATUS", nullable = false)
    @ColumnDefault(value = "'ACTIVE'")
    private GroupStatus status;              // 그룹 상태

    @Builder
    public GroupInfo(DiseaseInfo diseaseInfo, int score, GroupStatus status) {
        this.diseaseInfo = diseaseInfo;
        this.score = score;
        this.status = status;
    }
}
