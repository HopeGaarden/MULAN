package com.example.back.domain.auth.disease.repository;

import com.example.back.domain.auth.disease.DiseaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiseaseInfoRepository extends JpaRepository<DiseaseInfo, Long> {
    Optional<List<DiseaseInfo>> findByCode(String code);

    Optional<List<DiseaseInfo>> findByNameAndCode(String name, String code);

}
