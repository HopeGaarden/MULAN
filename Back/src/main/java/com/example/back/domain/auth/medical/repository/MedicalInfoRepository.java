package com.example.back.domain.auth.medical.repository;

import com.example.back.domain.auth.medical.MedicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Long> {
}
