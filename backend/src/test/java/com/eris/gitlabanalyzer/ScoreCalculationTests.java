package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffScore;
import com.eris.gitlabanalyzer.model.GitLabFileChange;
import com.eris.gitlabanalyzer.model.GitLabMergeRequest;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest
class ScoreCalculationTests {

    @Autowired
    private GitLabService gitLabService;
    private CalculateDiffScore calculateDiffScore = new CalculateDiffScore();

    @Test
    void check_MergeDiff() {
        long projectId = 2L;
        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, 3L).toIterable();
        calculateDiffScore.calculateScore(mr);
    }

}
