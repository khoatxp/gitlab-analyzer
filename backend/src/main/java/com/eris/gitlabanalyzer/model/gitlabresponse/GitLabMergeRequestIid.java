package com.eris.gitlabanalyzer.model.gitlabresponse;

public class GitLabMergeRequestIid {
    private Long iid;

    public GitLabMergeRequestIid(){}

    public Long getIid() {
        return iid;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequestIid{" +
                "iid=" + iid +
                '}';
    }
}
