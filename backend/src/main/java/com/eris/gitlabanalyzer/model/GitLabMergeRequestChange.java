package com.eris.gitlabanalyzer.model;

import java.util.List;

public class GitLabMergeRequestChange {

    private List<GitLabFileChange> changes;

    public GitLabMergeRequestChange() {
    }

    public List<GitLabFileChange> getChanges() {
        return changes;
    }

}
