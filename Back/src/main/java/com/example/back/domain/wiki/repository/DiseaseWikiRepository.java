package com.example.back.domain.wiki.repository;

import com.example.back.domain.wiki.DiseaseWiki;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface DiseaseWikiRepository extends JpaRepository<DiseaseWiki, Long> {
    boolean existsByDiseaseInfoId(Long diseaseInfoId);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<DiseaseWiki> findById(Long id);
}

