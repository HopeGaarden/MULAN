package com.example.back.domain.wiki.repository;

import com.example.back.domain.wiki.DiseaseWiki;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseWikiRepository extends JpaRepository<DiseaseWiki, Long> {
    boolean existsByDiseaseInfoId(Long diseaseInfoId);

    Optional<DiseaseWiki> findByDiseaseInfoId(Long diseaseInfoId);
}

