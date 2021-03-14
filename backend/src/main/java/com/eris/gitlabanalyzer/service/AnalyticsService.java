package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private ProjectService projectService;
    private final GitManagementUserService gitManagementUserService;
    private final MergeRequestService mergeRequestService;
    private final CommitService commitService;
    private final IssueService issueService;
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final UserServerRepository userServerRepository;
    private final UserProjectPermissionRepository userProjectPermissionRepository;
    private String serverUrl;
    private String accessToken;

    @Autowired
    public AnalyticsService(GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService, IssueService issueService, ProjectRepository projectRepository, ServerRepository serverRepository, UserServerRepository userServerRepository, UserProjectPermissionRepository userProjectPermissionRepository) {
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.userServerRepository = userServerRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
    }

    public AnalyticsService(GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService, IssueService issueService, ProjectRepository projectRepository, ServerRepository serverRepository, UserServerRepository userServerRepository, UserProjectPermissionRepository userProjectPermissionRepository, String serverUrl, String accessToken) {
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
        this.issueService = issueService;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.userServerRepository = userServerRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
        this.projectService = new ProjectService(this.projectRepository, this.serverRepository, this.userServerRepository,
                this.userProjectPermissionRepository, this.serverUrl, this.accessToken);
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
