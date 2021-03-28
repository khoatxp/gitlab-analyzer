package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
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
    private final GitLabService requestScopeGitLabService;

    public GitManagementUserService(GitManagementUserRepository gitManagementUserRepository, ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService requestScopeGitLabService) {
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    public void saveGitManagementUserInfo(Project project){
        Server server = project.getServer();

        var gitLabMembers = requestScopeGitLabService.getMembers(project.getGitLabProjectId());
        var gitLabMemberList= gitLabMembers.collectList().block();
        gitLabMemberList.forEach(gitLabMember -> {
                    GitManagementUser gitManagementUser= gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabMember.getId(), server.getId());
                    if (gitManagementUser == null){
                        gitManagementUser = new GitManagementUser(
                                gitLabMember.getId(),
                                gitLabMember.getUsername(),
                                gitLabMember.getName(),
                                server
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


    public List<GitManagementUserView> getMembers(Long projectId){
        return gitManagementUserRepository.findByProjectId(projectId);
    }
}
