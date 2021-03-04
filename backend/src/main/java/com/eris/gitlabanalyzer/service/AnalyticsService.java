package com.eris.gitlabanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectService projectService;
    private final GitManagementUserService gitManagementUserService;
    private final MergeRequestService mergeRequestService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public AnalyticsService(ProjectService projectService, GitManagementUserService gitManagementUserService, MergeRequestService mergeRequestService) {
        this.projectService = projectService;
        this.gitManagementUserService = gitManagementUserService;
        this.mergeRequestService = mergeRequestService;
    }

    public void saveAllFromGitlab(List<Long> gitLabProjectIdList, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        for (Long gitLabProjectId : gitLabProjectIdList) {
            projectService.saveProjectInfo(gitLabProjectId);
            gitManagementUserService.saveGitManagementUserInfo(gitLabProjectId);
            mergeRequestService.saveMergeRequestInfo(gitLabProjectId, startDateTime, endDateTime);
        }
    }
}
