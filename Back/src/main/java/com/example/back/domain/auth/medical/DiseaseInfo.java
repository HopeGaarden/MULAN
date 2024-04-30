package com.example.back.domain.auth.medical;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseInfo {
    private String code;        // 질병 코드
    private String name;        // 병명
}
