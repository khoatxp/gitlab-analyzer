package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class ScoreService {

    private final DiffScoreCalculator diffScoreCalculator;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    @Autowired
    public ScoreService(DiffScoreCalculator diffScoreCalculator){
        this.diffScoreCalculator = diffScoreCalculator;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getMergeDiffScore(Long projectId, Long mergeRequestIid){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        Iterable<GitLabFileChange> mr = gitLabService.getMergeRequestDiff(projectId, mergeRequestIid).toIterable();
        return diffScoreCalculator.calculateScore(mr);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalMergeDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        Iterable<GitLabMergeRequest> mergeRequests = gitLabService.getMergeRequests(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for( GitLabMergeRequest mr : mergeRequests){
          totalScore += diffScoreCalculator.calculateScore(gitLabService.getMergeRequestDiff(projectId, mr.getIid()).toIterable());
        }

        return totalScore;
    }

    // This will most likely change as we update how we retrieve diff's
    public int getCommitDiffScore(Long projectId, String sha){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        Iterable<GitLabFileChange> commit = gitLabService.getCommitDiff(projectId, sha).toIterable();
        return diffScoreCalculator.calculateScore(commit);
    }

    // This will most likely change as we update how we retrieve diff's
    public int getTotalCommitDiffScore(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        Iterable<GitLabCommit> commits = gitLabService.getCommits(projectId, startDateTime, endDateTime).toIterable();
        int totalScore = 0;
        for (GitLabCommit commit : commits) {
            totalScore += diffScoreCalculator.calculateScore(gitLabService.getCommitDiff(projectId, commit.getSha()).toIterable());
        }
        return totalScore;
    }

}
