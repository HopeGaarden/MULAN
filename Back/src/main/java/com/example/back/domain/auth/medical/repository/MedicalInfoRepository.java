package com.example.back.domain.auth.medical.repository;

import com.example.back.domain.auth.medical.MedicalInfo;
import com.example.back.domain.auth.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Long> {
    Optional<MedicalInfo> findByMember(Member member);
}
