package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.User;
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
    private final AnalysisRunService analysisRunService;

    public AnalyticsService(
            ProjectService projectService,
            GitManagementUserService gitManagementUserService,
            MergeRequestService mergeRequestService,
            CommitService commitService,
            IssueService issueService,
            AnalysisRunService analysisRunService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
        this.analysisRunService = analysisRunService;
    }

    public List<Long> saveAllFromGitlab(User user, List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        List<Long> projectIds = new ArrayList<>();
        gitLabProjectIdList.forEach(gitLabProjectId -> {
            Project project = projectService.saveProjectInfo(user, gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(project);
            mergeRequestService.saveMergeRequestInfo(project, startDateTime, endDateTime);
            commitService.saveCommitInfo(project, startDateTime, endDateTime);
            issueService.saveIssueInfo(project, startDateTime, endDateTime);
            projectIds.add(project.getId());
            this.analysisRunService.createAnalysisRun(user, project, startDateTime, endDateTime);
        });
        return projectIds;
    }
}
