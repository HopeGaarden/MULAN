package com.example.back.domain.auth.disease.repository;

import com.example.back.domain.auth.disease.DiseaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseInfoRepository extends JpaRepository<DiseaseInfo, Long> {

}
