package com.example.back.domain.auth.disease;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "DISEASE_INFO")
public class DiseaseInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISEASE_INFO_ID")
    private Long id;

    @Column(name = "CODE", nullable = false)
    private String code;        // 질병 코드

    @Column(name = "NAME", nullable = false)
    private String name;        // 병명

    @Column(name = "STANDARD", nullable = false, columnDefinition = "TEXT")
    private String standard;        // 검사기준

    @Column(name = "SCORE")
    private int score; // 그룹 점수

    @Builder
    public DiseaseInfo(String code, String name, String standard,int score) {
        this.code = code;
        this.name = name;
        this.standard = standard;
        this.score = score;
    }
}
