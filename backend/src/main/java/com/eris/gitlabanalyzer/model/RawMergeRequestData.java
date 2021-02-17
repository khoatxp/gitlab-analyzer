package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import reactor.core.publisher.Flux;

import java.util.List;

public class RawMergeRequestData {
    private Flux<RawCommitData> rawCommitData;
    private Flux<GitLabFileChange> gitLabDiff;
    private GitLabMergeRequest gitLabMergeRequest;

    public RawMergeRequestData() {
    }

    @JsonIgnore
    public Flux<RawCommitData> getFluxRawCommitData() {
        return rawCommitData;
    }

    public List<RawCommitData> getRawCommitData() {
        return rawCommitData.collectList().block();
    }

    public void setRawCommitData(Flux<RawCommitData> rawCommitData) {
        this.rawCommitData = rawCommitData;
    }

    public List<GitLabFileChange> getGitLabDiff() {
        return gitLabDiff.collectList().block();
    }

    public void setGitLabDiff(Flux<GitLabFileChange> gitLabDiff) {
        this.gitLabDiff = gitLabDiff;
    }

    public GitLabMergeRequest getGitLabMergeRequest() {
        return gitLabMergeRequest;
    }

    public void setGitLabMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        this.gitLabMergeRequest = gitLabMergeRequest;
    }
}
