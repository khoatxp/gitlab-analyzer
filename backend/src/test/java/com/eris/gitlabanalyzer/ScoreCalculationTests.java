package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoreCalculationTests {

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    private GitLabService gitLabService =  new GitLabService(serverUrl, accessToken);
    private final DiffScoreCalculator diffScoreCalculator = new DiffScoreCalculator();

    private final ZoneOffset zoneId = ZoneOffset.UTC;
    private final OffsetDateTime startTime = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 1, zoneId);
    private final OffsetDateTime endTime = OffsetDateTime.now();
    private final String diff = "+ code line 1 -2\n" +
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
