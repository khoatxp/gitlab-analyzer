package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final GitLabService gitLabService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public ProjectService(ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.gitLabService = gitLabService;
    }

    public void saveProjectInfo(Long gitLabProjectId) {
        if(projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl) != null){
            return;
        }

        var gitLabProject = gitLabService.getProject(gitLabProjectId).block();
        Project project = new Project(
                gitLabProjectId,
                gitLabProject.getName(),
                gitLabProject.getNameWithNamespace(),
                gitLabProject.getWebUrl(),
                serverRepository.findByServerUrlAndAccessToken(serverUrl,accessToken)
        );
        projectRepository.save(project);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
