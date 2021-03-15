package com.eris.gitlabanalyzer.viewmodel;

public interface CommitAuthorView {
    String getAuthorEmail();
    String getAuthorName();
    String getMappedGitManagementUserName();
    String getMappedGitManagementUserId();
}
