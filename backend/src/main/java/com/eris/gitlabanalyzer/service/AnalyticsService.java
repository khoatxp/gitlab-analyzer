package com.eris.gitlabanalyzer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectService projectService;
    private final GitLabUserService gitLabUserService;
    private final MergeRequestService mergeRequestService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public AnalyticsService(ProjectService projectService, GitLabUserService gitLabUserService, MergeRequestService mergeRequestService) {
        this.projectService = projectService;
        this.gitLabUserService = gitLabUserService;
        this.mergeRequestService = mergeRequestService;
    }

    public void saveAllFromGitlab(List<Long> gitLabProjectIdList, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        for (Long gitLabProjectId : gitLabProjectIdList) {
            projectService.saveProjectInfo(gitLabProjectId);
            gitLabUserService.saveGitlabUserInfo(gitLabProjectId);
            mergeRequestService.saveMergeRequestInfo(gitLabProjectId, startDateTime, endDateTime);
        }
    }
}
