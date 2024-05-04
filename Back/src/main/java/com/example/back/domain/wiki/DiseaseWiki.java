package com.example.back.domain.wiki;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DiseaseWiki {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DISEASE_WIKI_ID")
    private Long id;

    @Column(name = "DISEASE_INFO_ID", nullable = false)
    private Long diseaseInfoId;            // 질병 정보 아이디

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "AUTHOR", nullable = false)
    private Long authorId;                    // 마지막 수정자

    @Builder
    public DiseaseWiki(Long diseaseInfoId, String content, Long authorId) {
        this.diseaseInfoId = diseaseInfoId;
        this.content = content;
        this.authorId = authorId;
    }

    public void updateWiki(Long authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }
}
