package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reactor.core.publisher.Flux;

import java.util.List;

public class RawCommitData {
    private GitLabCommit gitLabCommit;
    private Flux<GitLabFileChange> gitLabDiff;

    public RawCommitData() {
    }

    public RawCommitData(GitLabCommit gitLabCommit, Flux<GitLabFileChange> gitLabDiff) {
        this.gitLabCommit = gitLabCommit;
        this.gitLabDiff = gitLabDiff;
    }

    public GitLabCommit getGitLabCommit() {
        return gitLabCommit;
    }

    @JsonIgnore
    public GitLabCommit getFluxGitLabCommit() {
        return gitLabCommit;
    }

    public List<GitLabFileChange> getGitLabDiff() {
        return gitLabDiff.collectList().block();
    }

}
