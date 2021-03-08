package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.CalculateDiffMetrics;
import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import com.eris.gitlabanalyzer.repository.FileScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ScoreService {

    private final GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator;
    private final CalculateDiffMetrics calculateDiffMetrics;
    private final FileScoreRepository fileScoreRepository;

    @Autowired
    public ScoreService(GitLabService gitLabService, DiffScoreCalculator diffScoreCalculator,
                        CalculateDiffMetrics calculateDiffMetrics,FileScoreRepository fileScoreRepository){
        this.diffScoreCalculator = diffScoreCalculator;
        this.gitLabService = gitLabService;
        this.calculateDiffMetrics = calculateDiffMetrics;
        this.fileScoreRepository = fileScoreRepository;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getMergeDiffScore(Long projectId, Long mergeRequestId){
        calculateDiffMetrics.storeMetricsMerge(mergeRequestId, projectId);
        return diffScoreCalculator.calculateScore(mergeRequestId);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalMergeDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        Iterable<GitLabMergeRequest> mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for( GitLabMergeRequest mr : mergeRequests){
          totalScore += diffScoreCalculator.calculateScore(mr.getId());
        }

        return totalScore;
    }
/*
    // This will most likely change as we update how we retrieve diff's
    public int getCommitDiffScore(Long projectId, String sha){
        Iterable<GitLabFileChange> commit = gitLabService.getCommitDiff(projectId, sha).toIterable();
        return diffScoreCalculator.calculateScore(commit);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalCommitDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabCommit commit : commits) {
            totalScore += diffScoreCalculator.calculateScore(gitLabService.getCommitDiff(projectId, commit.getSha()).toIterable());
        }
        return totalScore;
    }
*/
}
