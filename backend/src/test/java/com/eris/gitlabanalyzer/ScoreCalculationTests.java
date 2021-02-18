package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffScore;
import com.eris.gitlabanalyzer.model.GitLabFileChange;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreCalculationTests {

    @Autowired
    private GitLabService gitLabService;
    private final CalculateDiffScore calculateDiffScore = new CalculateDiffScore();

    @Test
    void check_MergeDiff() {
        long projectId = 2L;
        // TODO modify retrieval to use database in future
        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, 3L).toIterable();
        assertNotNull(mr);
        assertTrue(mr.iterator().hasNext());
        int results = calculateDiffScore.calculateScore(mr);
        assertTrue((results > 0));
        System.out.println(results);
    }

}
