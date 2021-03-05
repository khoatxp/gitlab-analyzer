package com.eris.gitlabanalyzer.service;

import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectService projectService;
    private final GitManagementUserService gitManagementUserService;
    private final MergeRequestService mergeRequestService;
    private final CommitService commitService;
    private final IssueService issueService;

    public AnalyticsService(ProjectService projectService, GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService, IssueService issueService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
    }

    public void saveAllFromGitlab(List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        for (Long gitLabProjectId : gitLabProjectIdList) {
            projectService.saveProjectInfo(gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(gitLabProjectId);
            mergeRequestService.saveMergeRequestInfo(gitLabProjectId, startDateTime, endDateTime);
            commitService.saveCommitInfo(gitLabProjectId, startDateTime, endDateTime);
            issueService.saveIssueInfo(gitLabProjectId, startDateTime, endDateTime);
        }
    }
}
