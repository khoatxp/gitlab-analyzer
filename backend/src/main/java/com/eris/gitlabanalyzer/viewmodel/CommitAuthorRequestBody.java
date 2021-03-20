package com.eris.gitlabanalyzer.viewmodel;

@lombok.Getter
public class CommitAuthorRequestBody {
    private String authorName;
    private String authorEmail;
    private String mappedGitManagementUserName;
    private Long mappedGitManagementUserId;
    public CommitAuthorRequestBody(){}
}
