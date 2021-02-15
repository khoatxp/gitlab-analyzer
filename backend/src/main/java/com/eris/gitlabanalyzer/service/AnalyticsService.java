package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalyticsService {
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final GitLabService gitLabService;
    private final ProjectService projectService;
    private final MemberService memberService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public AnalyticsService(ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService gitLabService, ProjectService projectService, MemberService memberService) {
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.gitLabService = gitLabService;
        this.projectService = projectService;
        this.memberService = memberService;
    }

    public void saveAllFromGitlab(List<Long> projectIdList) {
        for (Long projectId : projectIdList) {
            Project project = projectService.saveProjectInfo(projectId);
            memberService.saveMemberInfo(project);
        }
    }
}
