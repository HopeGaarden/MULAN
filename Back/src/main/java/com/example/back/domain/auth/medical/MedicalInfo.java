package com.example.back.domain.auth.medical;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.medical.constant.VerifyStatus;
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
public class MedicalInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEDICAL_INFO_ID")
    private Long id;                            // 진단서 식별 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;                      // 진단서 주인 정보

    @Column(name = "DISEASE_INFO_ID")
    private Long diseaseInfoId;            // 질병 정보 아이디

    @Column(name = "HOSPITAL_INFO")
    private String hospitalInfo;                // 병원 정보(이름)

    @Enumerated(value = EnumType.STRING)
    @Column(name = "VERIFY_STATUS", nullable = false)
    @ColumnDefault(value = "'PENDING'")
    private VerifyStatus status;                // 진단서 진위여부 진행 상태

    @Column(name = "REGISTRATION_ID")
    private String registrationID;              // 문서 등록 번호

    @Column(name = "CONFIRMATION_DATE")
    private String confirmationDate;            // 진단서 확인 날짜

    @Builder
    public MedicalInfo(Member member, Long diseaseInfoId, String hospitalInfo, VerifyStatus status, String registrationID, String confirmationDate) {
        this.member = member;
        this.diseaseInfoId = diseaseInfoId;
        this.hospitalInfo = hospitalInfo;
        this.status = status;
        this.registrationID = registrationID;
        this.confirmationDate = confirmationDate;
    }
}
