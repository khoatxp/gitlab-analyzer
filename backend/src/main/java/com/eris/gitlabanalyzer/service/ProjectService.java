package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitLabMember;
import com.eris.gitlabanalyzer.model.GitLabProject;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final GitLabService gitLabService;

    public ProjectService(ProjectRepository projectRepository, GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.gitLabService = gitLabService;
    }

    public Project saveProjectInfo(Long projectId) {
        var gitLabProject = gitLabService.getProject(projectId).block();
        // TODO Check if project already exists
        Project project = new Project(
                projectId,
                gitLabProject.getName(),
                gitLabProject.getNameWithNamespace(),
                gitLabProject.getWebUrl()
        );

        return projectRepository.save(project);
    }

    public void getProjectAnalytics(List<Long> projectIdList) {
        for (Long projectId : projectIdList){
            Mono<GitLabProject> gitLabProject = gitLabService.getProject(projectId);
            System.out.println(gitLabProject);
            Flux<GitLabMember> gitLabMembers = gitLabService.getMembers(projectId);
            System.out.println(gitLabMembers);
        }
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
