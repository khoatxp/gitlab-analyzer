package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.model.Server;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.viewmodel.GitManagementUserView;
import org.springframework.stereotype.Service;

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
        var gitLabLeftMembers = requestScopeGitLabService.getMembersThatLeftProject(project.getGitLabProjectId());

        var gitLabMemberList = gitLabMembers.collectList().blockOptional().orElse(new ArrayList<>());
        gitLabMemberList.addAll(gitLabLeftMembers.collectList().blockOptional().orElse(new ArrayList<>()));

        gitLabMemberList.stream().distinct().forEach(gitLabMember -> {
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
    public GitManagementUserView getMember(Long gitManagementUserId){
        return gitManagementUserRepository.findByGitManagementUserId(gitManagementUserId);
    }
}
