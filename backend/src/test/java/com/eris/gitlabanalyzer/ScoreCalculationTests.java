package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffScore;
import com.eris.gitlabanalyzer.model.GitLabFileChange;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreCalculationTests {

    @Autowired
    private GitLabService gitLabService;
    private final CalculateDiffScore calculateDiffScore = new CalculateDiffScore();

    private final ZoneId zoneId = ZoneId.systemDefault();
    private final ZonedDateTime startTime = ZonedDateTime.of(2015, 1, 1, 1, 1, 1, 1, zoneId);
    private final ZonedDateTime endTime = ZonedDateTime.now();

    @Test
    void check_MergeDiff() {
        long projectId = 2L;
        // TODO modify retrieval to use database in future
        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, 3L).toIterable();
        assertNotNull(mr);
        assertTrue(mr.iterator().hasNext());
        // check multiple files
        int results = calculateDiffScore.calculateScore(mr);
        assertTrue((results > 0));

        // check single file
        results = calculateDiffScore.calculateScore(mr.iterator().next());
        assertTrue((results > 0));
    }
    @Test
    void check_commitDiff() {
        long projectId = 2L;
        String sha = gitLabService.getCommits(projectId, startTime, endTime).toIterable().iterator().next().getSha();
        Iterable<GitLabFileChange> commit = gitLabService.getCommitDiff(projectId, sha).toIterable();
        assertNotNull(commit);
        assertTrue(commit.iterator().hasNext());
        // check multiple files
        int results = calculateDiffScore.calculateScore(commit);
        assertTrue((results > 0));

        // check single file
        results = calculateDiffScore.calculateScore(commit.iterator().next());
        assertTrue((results > 0));
    }

}
