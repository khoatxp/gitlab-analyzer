package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
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

    public void saveGitManagementUserInfo(Long gitLabProjectId){
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabUsers = gitLabService.getMembers(gitLabProjectId);
        var gitLabUserList= gitLabUsers.collectList().block();

        if (gitLabUserList != null && !gitLabUserList.isEmpty()) {
            gitLabUserList.forEach(gitLabMember -> {
                GitManagementUser gitManagementUser= gitManagementUserRepository.findByUserNameAndServerUrl(gitLabMember.getUsername(),serverUrl);
                if (gitManagementUser == null){
                    gitManagementUser = new GitManagementUser(
                            gitLabMember.getUsername(),
                            gitLabMember.getName(),
                            serverRepository.findByServerUrlAndAccessToken(serverUrl,accessToken)
                    );
                }
                gitManagementUser.addProject(project);
                gitManagementUserRepository.save(gitManagementUser);
            });
        }
    }

    public List<GitManagementUser> getMembersByProjectId(Long projectId){
        return gitManagementUserRepository.findByProjectId(projectId);
    }
}
