package com.example.back.api.wiki.service;

import com.example.back.api.wiki.controller.request.DiseaseWikiPatchRequest;
import com.example.back.api.wiki.service.response.WikiResolveConflictResponse;
import com.example.back.common.utils.DiffDTO;
import com.example.back.common.utils.DiffService;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiseaseWikiServiceMockTest {

    @Mock
    private DiseaseWikiRepository diseaseWikiRepository;

    @InjectMocks
    private DiseaseWikiService diseaseWikiService;

    @Test
    void resolveConflict_ShouldReturnCorrectResponse() {
        // Arrange
        Long wikiId = 1L;
        String originalContent = "Hello World\nJava";
        String newContent = "Hello World\nJava and Spring";
        DiseaseWikiPatchRequest request = new DiseaseWikiPatchRequest(wikiId, newContent);
        DiseaseWiki wiki = new DiseaseWiki(wikiId, originalContent, 1L);

        when(diseaseWikiRepository.findById(wikiId)).thenReturn(java.util.Optional.of(wiki));

        // Act
        WikiResolveConflictResponse response = diseaseWikiService.resolveConflict(request);

        System.out.println("Original Lines: " + response.diffs().get(0).originalLines());
        System.out.println("New Lines: " + response.diffs().get(0).newLines());

        // Assert
        assertNotNull(response);
        assertEquals(originalContent, response.originalContent());
        assertEquals(newContent, response.newContent());
        assertEquals(1, response.diffs().size());
        assertEquals("Java", response.diffs().get(0).originalLines().get(0));
        assertEquals("Java and Spring", response.diffs().get(0).newLines().get(0));

        // Verify interactions
        verify(diseaseWikiRepository, times(1)).findById(wikiId);
        verifyNoMoreInteractions(diseaseWikiRepository);
    }
}
