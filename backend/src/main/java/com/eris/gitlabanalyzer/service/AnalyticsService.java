package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalyticsService {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final MergeRequestService mergeRequestService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public AnalyticsService(ProjectService projectService, MemberService memberService, MergeRequestService mergeRequestService) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.mergeRequestService = mergeRequestService;
    }

    public void saveAllFromGitlab(List<Long> projectIdList) {
        for (Long projectId : projectIdList) {
            projectService.saveProjectInfo(projectId);
            memberService.saveMemberInfo(projectId);
            mergeRequestService.saveMergeRequestInfo(projectId);
        }
    }
}
