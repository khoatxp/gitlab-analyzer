package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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

    public List<Long> saveAllFromGitlab(List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Long> projectIds = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            Project project = projectService.saveProjectInfo(gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(project);
            mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);
            commitService.saveCommitInfo(project, startDateTime, endDateTime);
            issueService.saveIssueInfo(project, startDateTime, endDateTime);
            projectIds.add(project.getId());
        });
        return projectIds;
    }
}
