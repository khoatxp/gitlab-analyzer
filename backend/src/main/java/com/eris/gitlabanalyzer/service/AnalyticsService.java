package com.eris.gitlabanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectService projectService;
    private final GitManagementUserService gitManagementUserService;
    private final MergeRequestService mergeRequestService;
    private final CommitService commitService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public AnalyticsService(ProjectService projectService, GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService, CommitService commitService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
        this.commitService = commitService;
    }

    public void saveAllFromGitlab(List<Long> gitLabProjectIdList, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        for (Long gitLabProjectId : gitLabProjectIdList) {
            projectService.saveProjectInfo(gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(gitLabProjectId);
            mergeRequestService.saveMergeRequestInfo(gitLabProjectId, startDateTime, endDateTime);
            commitService.saveCommitInfo(gitLabProjectId, startDateTime, endDateTime);

        }
    }
}
