package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class ScoreService {

    private final GitLabService gitLabService;
    private final DiffScoreCalculator diffScoreCalculator;

    @Autowired
    public ScoreService(GitLabService gitLabService, DiffScoreCalculator diffScoreCalculator){
        this.diffScoreCalculator = diffScoreCalculator;
        this.gitLabService = gitLabService;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getMergeDiffScore(Long projectId, Long mergeRequestIid){
        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, mergeRequestIid).toIterable();
        return diffScoreCalculator.calculateScore(mr);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalMergeDiffScore(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime){
        Iterable<GitLabMergeRequest> mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for( GitLabMergeRequest mr : mergeRequests){
          totalScore += diffScoreCalculator.calculateScore(gitLabService.getMergeRequestDiff(projectId, mr.getIid()).toIterable());
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getCommitDiffScore(Long projectId, String sha){
        Iterable<GitLabFileChange> commit = gitLabService.getCommitDiff(projectId, sha).toIterable();
        return diffScoreCalculator.calculateScore(commit);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalCommitDiffScore(Long projectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabCommit commit : commits) {
            totalScore += diffScoreCalculator.calculateScore(gitLabService.getCommitDiff(projectId, commit.getSha()).toIterable());
        }
        return totalScore;
    }

}
