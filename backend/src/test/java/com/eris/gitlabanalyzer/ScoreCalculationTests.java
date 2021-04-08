package com.eris.gitlabanalyzer;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.*;
import com.eris.gitlabanalyzer.service.GitLabService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
@WebAppConfiguration
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
    private ScoreProfileRepository scoreProfileRepository;
    @Autowired
    private GitLabService requestScopeGitLabService;

    @Autowired
    private GitLabService gitLabService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

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
            "+ /* \n" +
            "- stuff\n" +
            "+ Block comment line 1 -9\n" +
            "+ code line 4 -10\n" +
            "+ code line 5 -11\n" +
            "+ Block comment line 1 */ some code\n" +
            "+ code line 6 -14\n" +
            "/* start of comment not added\n" +
            "+ continuation that is added\n" +
            "*/ closing not added\n" +
            "+ }";
    @BeforeAll
     void setup(){
        server = serverRepository.findByServerUrl(serverUrl).orElse(null);
        if(server == null){
            server = new Server(serverUrl);
            serverRepository.save(server);
        }
        gitManagementUser= new GitManagementUser(1L, "testUsername", "testName", server);
        gitManagementUserRepository.save(gitManagementUser);

        project = new Project(projectId, "Test", "TestNameSpace", "webURl", server);
        projectRepository.save(project);
        gitLabService =  new GitLabService(serverUrl, accessToken);
    }

    @BeforeEach
    void setupEach() {
        requestScopeGitLabService.setServerUrl(serverUrl);
        requestScopeGitLabService.setAccessToken(accessToken);
    }

    @Test
    void check_MergeDiff()  {
        long mergeId = 3L;

        MergeRequest mergeRequest = new MergeRequest(mergeId, "testAuthor", "testTitle", startTime, startTime, "weburl", project, gitManagementUser);
        mergeRequestRepository.save(mergeRequest);

        calculateDiffMetrics.storeMetricsMerge(mergeRequest);
        double results = diffScoreCalculator.calculateScoreMerge(mergeRequest.getId(), 0L);
        assertTrue((results > 0));

    }

    @Test
    void check_CommitDiff()  {

        String sha = gitLabService.getCommits(projectId, startTime, endTime).toIterable().iterator().next().getSha();
        Commit commit = new Commit(sha, "testTitle", "Author", "email", startTime, "weburl", project);
        commitRepository.save(commit);


        calculateDiffMetrics.storeMetricsCommit(commit);
        double results = diffScoreCalculator.calculateScoreCommit(commit.getId(), 0L);
        assertTrue((results > 0));

    }

    @Test
    void check_ScoreCalculations() {
        double codeValue = 2;
        double commentValue = 1;
        double syntaxValue = 1;
        double removedVale = 0.5;
        Long mergeId = 1L;

        MergeRequest mergeRequest = new MergeRequest(mergeId, "testAuthor", "testTitle", startTime, startTime, "weburl", project, gitManagementUser);
        mergeRequestRepository.save(mergeRequest);

        calculateDiffMetrics.testCalculateLines(diff, "java", mergeRequest);
        double results = diffScoreCalculator.calculateScoreMerge(mergeRequest.getId(), 0L);
        double expectedValue = codeValue * 5;
        expectedValue += commentValue * 7;
        expectedValue += syntaxValue * 2;
        expectedValue += removedVale * 1;
        assertEquals(expectedValue, results);

        String profileName = "testProfile";

        ScoreProfile scoreProfile = new ScoreProfile(profileName, codeValue, removedVale, syntaxValue, commentValue );
        Map<String, Double> extensions = new HashMap<>();
        extensions.put("java", 2.0);
        scoreProfile.addExtension(extensions);
        scoreProfileRepository.save(scoreProfile);

        results = diffScoreCalculator.calculateScoreMerge(mergeRequest.getId(), scoreProfile.getId());
        expectedValue += codeValue * 5;
        assertEquals(expectedValue, results);
    }

}
