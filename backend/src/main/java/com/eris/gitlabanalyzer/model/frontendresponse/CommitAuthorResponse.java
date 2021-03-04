package com.eris.gitlabanalyzer.model.frontendresponse;

public interface CommitAuthorResponse {
    String getAuthorEmail();
    String getAuthorName();
    String getMappedGitManagementUserName();
    String getMappedGitManagementUserId();
}
