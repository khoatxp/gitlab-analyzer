package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabFileChange;

import java.util.List;

public class GitLabMergeRequestChange {

    private List<GitLabFileChange> changes;

    public GitLabMergeRequestChange() {
    }

    public List<GitLabFileChange> getChanges() {
        return changes;
    }

}
