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
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ScoreService {

    private GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final MergeRequestRepository mergeRequestRepository;
    private final CommitRepository commitRepository;
    private String serverUrl;
    private String accessToken;

    @Autowired
    public ScoreService(DiffScoreCalculator diffScoreCalculator, CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository, CommitRepository commitRepository) {
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
    }

    public ScoreService(DiffScoreCalculator diffScoreCalculator,
                        CalculateDiffMetrics calculateDiffMetrics, MergeRequestRepository mergeRequestRepository,
                        CommitRepository commitRepository,
                        String serverUrl,
                        String accessToken) {
        this.diffScoreCalculator = diffScoreCalculator;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
        gitLabService = new GitLabService(this.serverUrl, this.accessToken);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getMergeDiffScore(Long projectId, Long mergeRequestId) {
        calculateDiffMetrics.storeMetricsMerge(projectId, mergeRequestId);
        return diffScoreCalculator.calculateScoreMerge(mergeRequestId);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalMergeDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Iterable<GitLabMergeRequest> mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabMergeRequest gitLabMergeRequest : mergeRequests) {
            MergeRequest mr = mergeRequestRepository.findByIidAndProjectId(gitLabMergeRequest.getIid(), projectId);
            calculateDiffMetrics.storeMetricsMerge(projectId, mr.getId());
            totalScore += diffScoreCalculator.calculateScoreMerge(mr.getId());
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getCommitDiffScore(Long projectId, Long commitId) {
        calculateDiffMetrics.storeMetricsCommit(commitId, projectId);
        return diffScoreCalculator.calculateScoreCommit(commitId);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalCommitDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabCommit gitLabCommit : commits) {
            Commit commit = commitRepository.findByCommitShaAndProjectId(gitLabCommit.getSha(), projectId);
            calculateDiffMetrics.storeMetricsMerge(projectId, commit.getId());
            totalScore += diffScoreCalculator.calculateScoreMerge(commit.getId());
        }
        return totalScore;
    }

}
