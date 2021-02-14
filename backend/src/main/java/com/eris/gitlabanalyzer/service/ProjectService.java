//package com.eris.gitlabanalyzer.service;
//
//import com.eris.gitlabanalyzer.model.*;
//import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMember;
//import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
//import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequestIid;
//import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabProject;
//import com.eris.gitlabanalyzer.repository.ProjectRepository;
//import com.eris.gitlabanalyzer.repository.ServerRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Service
//public class ProjectService {
//    private final ProjectRepository projectRepository;
//    private final ServerRepository serverRepository;
//    private final GitLabService gitLabService;
//
//    @Value("${gitlab.SERVER_URL}")
//    String serverUrl;
//
//    @Value("${gitlab.ACCESS_TOKEN}")
//    String accessToken;
//
//    public ProjectService(ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService gitLabService) {
//        this.projectRepository = projectRepository;
//        this.serverRepository = serverRepository;
//        this.gitLabService = gitLabService;
//    }
//
//    public Project saveProjectInfo(Long projectId) {
//        var gitLabProject = gitLabService.getProject(projectId).block();
//        // TODO Check if project already exists
//        Project project = new Project(
//                projectId,
//                gitLabProject.getName(),
//                gitLabProject.getNameWithNamespace(),
//                gitLabProject.getWebUrl(),
//                serverRepository.find(serverUrl,accessToken)
//        );
//
//        return projectRepository.save(project);
//    }
//
//    public void getProjectAnalytics(List<Long> projectIdList) {
//        for (Long projectId : projectIdList){
//            Mono<GitLabProject> gitLabProject = gitLabService.getProject(projectId);
//            System.out.println(gitLabProject);
//
//            Flux<GitLabMember> gitLabMembers = gitLabService.getMembers(projectId);
//            System.out.println(gitLabMembers);
//
//            Flux<GitLabMergeRequestIid> gitLabMergeRequestIids = gitLabService.getMergedMergeRequestIids(projectId);
//
//            for(GitLabMergeRequestIid mergeRequestIid : gitLabMergeRequestIids.collectList().block()){
//                Mono<GitLabMergeRequest> gitLabMergeRequest = gitLabService.getMergeRequest(projectId,mergeRequestIid.getIid());
//                System.out.println(gitLabMergeRequest.block());
//            }
//        }
//    }
//
//    public List<Project> getProjects() {
//        return projectRepository.findAll();
//    }
//}
