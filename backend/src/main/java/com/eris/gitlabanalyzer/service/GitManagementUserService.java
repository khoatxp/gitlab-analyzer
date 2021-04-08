package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import org.springframework.http.HttpStatus;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitManagementUserService {
    private final GitManagementUserRepository gitManagementUserRepository;
    private final GitLabService requestScopeGitLabService;

    public GitManagementUserService(GitManagementUserRepository gitManagementUserRepository, GitLabService requestScopeGitLabService) {
        this.gitManagementUserRepository = gitManagementUserRepository;
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


    public List<GitManagementUser> getMembers(Long projectId){
        return gitManagementUserRepository.findByProjectId(projectId);
    }

    public GitManagementUser getMember(Long gitManagementUserId){
        return gitManagementUserRepository.findById(gitManagementUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found with id" + gitManagementUserId)
        );
    }
}
