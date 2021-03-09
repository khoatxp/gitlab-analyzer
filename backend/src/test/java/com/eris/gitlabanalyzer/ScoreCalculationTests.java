package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.*;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoreCalculationTests {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MergeRequestRepository mergeRequestRepository;
    @Autowired
    private CommitRepository commitRepository;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private GitManagementUserRepository gitManagementUserRepository;
    @Autowired
    private DiffScoreCalculator diffScoreCalculator;
    @Autowired
    private CalculateDiffMetrics calculateDiffMetrics;
    @Autowired
    private GitLabService gitLabService;


    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    private final ZoneOffset zoneId = ZoneOffset.UTC;
    private final OffsetDateTime startTime = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 1, zoneId);
    private final OffsetDateTime endTime = OffsetDateTime.now();
    private Server server;
    private GitManagementUser gitManagementUser;
    private Project project;

    private final long projectId = 2L;
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
    @BeforeAll
     void setup(){
        server = new Server(serverUrl);
        serverRepository.save(server);
        gitManagementUser= new GitManagementUser(1L, "testUsername", "testName", server);
        gitManagementUserRepository.save(gitManagementUser);

        project = new Project(projectId, "Test", "TestNameSpace", "webURl", server);
        projectRepository.save(project);

    }

    @Test
    void check_MergeDiff()  {
        long mergeId = 3L;

        MergeRequest mergeRequest = new MergeRequest(mergeId, "testAuthor", "testTitle", startTime, "weburl", project, gitManagementUser);
        mergeRequestRepository.save(mergeRequest);

        calculateDiffMetrics.storeMetricsMerge(project.getId(), mergeRequest.getId());
        int results = diffScoreCalculator.calculateScoreMerge(mergeRequest.getId());
        assertTrue((results > 0));

    }

    @Test
    void check_CommitDiff()  {

        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startTime, endTime).toIterable();
        String sha = commits.iterator().next().getSha();
        Commit commit = new Commit(sha, "testTitle", "Author", "email", startTime, "weburl", project);
        commitRepository.save(commit);

        calculateDiffMetrics.storeMetricsCommit(project.getId(), commit.getId());
        int results = diffScoreCalculator.calculateScoreCommit(commit.getId());
        assertTrue((results > 0));

    }

    @Test
    void check_ScoreCalculations() {
        // TODO get actual values being set so test pass if point values change
        int javaCodePointval = 2;
        int commentValue = 1;
        int syntaxValue = 1;
        Long mergeId = 1L;

        MergeRequest mergeRequest = new MergeRequest(mergeId, "testAuthor", "testTitle", startTime, "weburl", project, gitManagementUser);
        mergeRequestRepository.save(mergeRequest);

        calculateDiffMetrics.testCalculateLines(diff, "java", mergeRequest);
        int results = diffScoreCalculator.calculateScoreMerge(mergeRequest.getId());
        int expectedValue = javaCodePointval * 4;
        expectedValue += commentValue * 6;
        expectedValue += syntaxValue;
        assertEquals(expectedValue, results);
    }

}
