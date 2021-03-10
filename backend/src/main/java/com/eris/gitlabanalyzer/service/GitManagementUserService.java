package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.frontendresponse.GitManagementUserResponse;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitManagementUserService {
    private final GitManagementUserRepository gitManagementUserRepository;
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final GitLabService gitLabService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public GitManagementUserService(GitManagementUserRepository gitManagementUserRepository, ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService gitLabService) {
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.gitLabService = gitLabService;
    }

    public void saveGitManagementUserInfo(Project project){
        var gitLabMembers = gitLabService.getMembers(project.getGitLabProjectId());
        var gitLabMemberList= gitLabMembers.collectList().block();
        gitLabMemberList.forEach(gitLabMember -> {
                    GitManagementUser gitManagementUser= gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabMember.getId(),serverUrl);
                    if (gitManagementUser == null){
                        gitManagementUser = new GitManagementUser(
                                gitLabMember.getId(),
                                gitLabMember.getUsername(),
                                gitLabMember.getName(),
                                serverRepository.findByServerUrlAndAccessToken(serverUrl,accessToken)
                        );
                    }

                    GitManagementUser checkIfAlreadyInProject = gitManagementUserRepository.findByGitLabUserIdAndProjectId(gitLabMember.getId(),project.getId());
                    if(checkIfAlreadyInProject == null){
                        gitManagementUser.addProject(project);
                    }

                    gitManagementUserRepository.save(gitManagementUser);
                }
        );
    }


    public List<GitManagementUserResponse> getMembersByProjectId(Long projectId){
        return gitManagementUserRepository.getByProjectId(projectId);
    }
}
