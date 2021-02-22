package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.dataprocessing.DiffScoreCalculator;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int getCommitDiffScore(Long projectId, String sha){
        Iterable<GitLabFileChange> commit = gitLabService.getCommitDiff(projectId, sha).toIterable();
        return diffScoreCalculator.calculateScore(commit);
    }

}
