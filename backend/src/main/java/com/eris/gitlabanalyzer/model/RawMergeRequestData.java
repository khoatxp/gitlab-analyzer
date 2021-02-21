package com.eris.gitlabanalyzer.model;

import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import reactor.core.publisher.Flux;

import java.util.List;

public class RawMergeRequestData {
    private Flux<RawCommitData> rawCommitData;
    private Flux<GitLabFileChange> gitLabDiff;
    private GitLabMergeRequest gitLabMergeRequest;

    public RawMergeRequestData() {
    }

    public RawMergeRequestData(Flux<RawCommitData> rawCommitData,
                               Flux<GitLabFileChange> gitLabDiff,
                               GitLabMergeRequest gitLabMergeRequest) {
        this.rawCommitData = rawCommitData;
        this.gitLabDiff = gitLabDiff;
        this.gitLabMergeRequest = gitLabMergeRequest;
    }

    @JsonIgnore
    public Flux<RawCommitData> getFluxRawCommitData() {
        return rawCommitData;
    }

    public List<RawCommitData> getRawCommitData() {
        return rawCommitData.collectList().block();
    }

    public List<GitLabFileChange> getGitLabDiff() {
        return gitLabDiff.collectList().block();
    }

    public GitLabMergeRequest getGitLabMergeRequest() {
        return gitLabMergeRequest;
    }

}
