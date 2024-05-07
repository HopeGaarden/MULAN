package com.example.back.domain.auth.disease.repository;

import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseMemberRepository extends JpaRepository<DiseaseMember, Long> {
    Optional<DiseaseMember> findByMember(Member member);

    Optional<DiseaseMember> findByMemberAndDiseaseInfoId(Member member, Long DiseaseInfoId);

}
