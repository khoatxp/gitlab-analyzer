package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AnalyticsService(ProjectService projectService, GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService, IssueService issueService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
    }

    public void saveAllFromGitlab(List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            Project project = projectService.saveProjectInfo(gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(project);
            mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);
            commitService.saveCommitInfo(project, startDateTime, endDateTime);
            issueService.saveIssueInfo(project, startDateTime, endDateTime);
        });
    }
}
