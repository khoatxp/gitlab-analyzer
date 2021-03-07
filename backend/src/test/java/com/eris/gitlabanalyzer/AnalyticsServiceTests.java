package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.service.AnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnalyticsServiceTests {
    @Autowired
    private AnalyticsService analyticsService;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testSaveAllErrorWithNonExistentProjectIds() {
        Long[] fakeProjectIds = new Long[]{91298283L, 9273477L};

        assertThrows(WebClientResponseException.NotFound.class, () -> {
            analyticsService.saveAllFromGitlab(Arrays.asList(fakeProjectIds), OffsetDateTime.now(), OffsetDateTime.now());
        });
    }

    @Test
    void testSaveAll() {
        long gitlabAnalyzerProjectId = 5L;
        Long[] projectIds = new Long[]{gitlabAnalyzerProjectId};

        assertEquals(projectRepository.findAll().size(), 0);
        analyticsService.saveAllFromGitlab(Arrays.asList(projectIds), OffsetDateTime.now(), OffsetDateTime.now());

        assertEquals(projectRepository.findAll().size(), 1);
        // TODO: add more assertions for other repositories as more data gets saved with this function
    }
}
