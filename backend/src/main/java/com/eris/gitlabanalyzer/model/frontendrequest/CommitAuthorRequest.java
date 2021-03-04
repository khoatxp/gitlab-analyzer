package com.eris.gitlabanalyzer.model.frontendrequest;

@lombok.Getter
public class CommitAuthorRequest {
    private String authorName;
    private String authorEmail;
    private String mappedGitManagementUserName;
    private Long mappedGitManagementUserId;
    public CommitAuthorRequest(){}
}
