package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import com.eris.gitlabanalyzer.repository.CommitRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ScoreService {

    private GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final MergeRequestRepository mergeRequestRepository;
    private final CommitRepository commitRepository;
    private final String serverUrl;
    private final String accessToken;

    @Autowired
    public ScoreService(DiffScoreCalculator diffScoreCalculator,
                        CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository,
                        CommitRepository commitRepository,
                        @Value("${gitlab.SERVER_URL}") String serverUrl, // TODO Remove after server info is correctly retrieved based on internal projectId
                        @Value("${gitlab.ACCESS_TOKEN}") String accessToken) {
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
        gitLabService = new GitLabService(this.serverUrl, this.accessToken);
    }

    // This will most likely change as we update how we retrieve diff's
    public double getMergeDiffScore( Long mergeRequestId){
        return diffScoreCalculator.calculateScoreMerge(mergeRequestId);
    }

    public void saveMergeDiffMetrics(MergeRequest mergeRequest){
        calculateDiffMetrics.storeMetricsMerge(mergeRequest);
    }

    public double getTotalMergeDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<MergeRequest> mergeRequests = mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
        double totalScore = 0;
        for( MergeRequest mr : mergeRequests){
            totalScore += diffScoreCalculator.calculateScoreMerge(mr.getId());
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public double getCommitDiffScore(Long commitId){

        return diffScoreCalculator.calculateScoreCommit(commitId);
    }

    public void saveCommitDiffMetrics(Commit commit){
        calculateDiffMetrics.storeMetricsCommit(commit);
    }


    // This will most likely change as we update how we retrieve diff's
    public double getTotalCommitDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Commit> commits = commitRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
        double totalScore = 0;
        for (Commit commit : commits) {
            totalScore += diffScoreCalculator.calculateScoreCommit(commit.getId());
        }
        return totalScore;
    }

}
