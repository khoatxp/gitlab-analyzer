package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.GitLabFileChange;
import com.eris.gitlabanalyzer.model.GitLabMergeRequestChange;
import com.eris.gitlabanalyzer.service.GitLabService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreCalculationTests {

    @Autowired
    private GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator = new DiffScoreCalculator();

    private final ZoneId zoneId = ZoneId.systemDefault();
    private final ZonedDateTime startTime = ZonedDateTime.of(2015, 1, 1, 1, 1, 1, 1, zoneId);
    private final ZonedDateTime endTime = ZonedDateTime.now();
    private String diff = "+ code line 1 -2\n" +
            "+ code line 2 -4\n" +
            "+ // comment line 1 -5\n" +
            "+ code line 3 -7\n" +
            "+ // comment line 1 -8\n" +
            "+ /* Block comment line 1 -9\n" +
            "+ code line 4 -10\n" +
            "+ code line 5 -11\n" +
            "+ Block comment line 1 */-12\n" +
            "+ code line 6 -14\n" +
            "+ }";

    @Test
    void check_MergeDiff()  {
        long projectId = 2L;
        // TODO modify retrieval to use database in future
        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, 3L).toIterable();
        assertNotNull(mr);
        assertTrue(mr.iterator().hasNext());
        // check multiple files
        int results = diffScoreCalculator.calculateScore(mr);
        assertTrue((results > 0));

        // check single file
        results = diffScoreCalculator.calculateScore(mr.iterator().next());
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
        int results = diffScoreCalculator.calculateScore(commit);
        assertTrue((results > 0));

        // check single file
        results = diffScoreCalculator.calculateScore(commit.iterator().next());
        assertTrue((results > 0));
    }

    @Test
    void check_ScoreCalculations() {
        // TODO get actual values being set so test pass if point values change
        int javaCodePointval = 2;
        int commentValue = 1;

        int results =  diffScoreCalculator.calculateScore(diff, "java");
        int expectedValue = javaCodePointval * 4;
        expectedValue += commentValue * 6;
        assertEquals(results, expectedValue);
    }

}
