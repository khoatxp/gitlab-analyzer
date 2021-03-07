package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.repository.*;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.method.P;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoreCalculationTests {

    @Autowired
    private GitLabService gitLabService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MergeRequestRepository mergeRequestRepository;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private GitManagementUserRepository gitManagementUserRepository;
    @Autowired
    private FileScoreRepository fileScoreRepository;
    @Autowired
    private DiffScoreCalculator diffScoreCalculator;
    @Autowired
    private CalculateDiffMetrics calculateDiffMetrics;


    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    private final ZoneOffset zoneId = ZoneOffset.UTC;
    private final OffsetDateTime startTime = OffsetDateTime.of(2015, 1, 1, 1, 1, 1, 1, zoneId);
    private Server server;
    private GitManagementUser gitManagementUser;
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
        gitManagementUser= new GitManagementUser("testUsername", "testName", server);
        gitManagementUserRepository.save(gitManagementUser);
    }


    @Test
    void check_MergeDiff()  {
        long projectId = 2L;
        long mergeId = 3L;


        Project project = new Project(projectId, "Test", "TestNameSpace", "webURl", server);
        projectRepository.save(project);

        MergeRequest mergeRequest = new MergeRequest(mergeId, "testAuthor", "testTitle", startTime, "weburl", project, gitManagementUser);
        mergeRequestRepository.save(mergeRequest);

        calculateDiffMetrics.storeMetrics(mergeId, projectId);

        int results = diffScoreCalculator.calculateScore(mergeId, projectId);
        assertTrue((results > 0));

    }

   /* @Test
    void check_ScoreCalculations() {
        // TODO get actual values being set so test pass if point values change
        int javaCodePointval = 2;
        int commentValue = 1;

        int results =  diffScoreCalculator.calculateScore(diff, "java");
        int expectedValue = javaCodePointval * 4;
        expectedValue += commentValue * 6;
        assertEquals(results, expectedValue);
    } */

}
