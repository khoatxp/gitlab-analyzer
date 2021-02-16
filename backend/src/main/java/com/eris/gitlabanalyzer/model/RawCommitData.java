package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reactor.core.publisher.Flux;

import java.util.List;

public class RawCommitData {
    private GitLabCommit gitLabCommit;
    private Flux<GitLabDiff> gitLabDiff;

    public RawCommitData() {
    }

    public GitLabCommit getGitLabCommit() {
        return gitLabCommit;
    }

    @JsonIgnore
    public GitLabCommit getFluxGitLabCommit() {
        return gitLabCommit;
    }

    public void setGitLabCommit(GitLabCommit gitLabCommit) {
        this.gitLabCommit = gitLabCommit;
    }

    public List<GitLabDiff> getGitLabDiff() {
        return gitLabDiff.collectList().block();
    }

    public void setGitLabDiff(Flux<GitLabDiff> gitLabDiff) {
        this.gitLabDiff = gitLabDiff;
    }
}
