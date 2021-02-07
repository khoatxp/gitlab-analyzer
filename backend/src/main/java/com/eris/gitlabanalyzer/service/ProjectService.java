package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Member;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.MemberRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final GitLabService gitLabService;

    public ProjectService(ProjectRepository projectRepository, MemberRepository memberRepository, GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.memberRepository = memberRepository;
        this.gitLabService = gitLabService;
    }

    public Project saveProjectInfo(Long projectId) {
        var project = gitLabService.getProject(projectId);
        // TODO Check if project already exists
        return projectRepository.save(project.block());
    }


    public void analyzeProjects(List<Long> projectIdList) {
        for (Long projectId : projectIdList){
            Mono<Project> project = gitLabService.getProject(projectId);
            System.out.println(project);
            Flux<Member> members = gitLabService.getMembers(projectId);
            System.out.println(members);
        }
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
