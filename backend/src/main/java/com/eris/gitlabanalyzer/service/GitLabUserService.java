package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitLabUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitLabUserRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitLabUserService {
    private final GitLabUserRepository gitLabUserRepository;
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final GitLabService gitLabService;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public GitLabUserService(GitLabUserRepository gitLabUserRepository, ProjectRepository projectRepository, ServerRepository serverRepository, GitLabService gitLabService) {
        this.gitLabUserRepository = gitLabUserRepository;
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.gitLabService = gitLabService;
    }

    public void saveGitlabUserInfo(Long gitLabProjectId){
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabUsers = gitLabService.getMembers(gitLabProjectId);
        var gitLabUserList= gitLabUsers.collectList().block();

        if (gitLabUserList != null && !gitLabUserList.isEmpty()) {
            gitLabUserList.forEach(gitLabMember -> {
                GitLabUser gitLabUser = gitLabUserRepository.findByUserNameAndServerUrl(gitLabMember.getUsername(),serverUrl);
                if (gitLabUser == null){
                    gitLabUser = new GitLabUser(
                            gitLabMember.getUsername(),
                            gitLabMember.getName(),
                            serverRepository.findByServerUrlAndAccessToken(serverUrl,accessToken)
                    );
                }
                gitLabUser.addProject(project);
                gitLabUserRepository.save(gitLabUser);
            });
        }
    }

    public List<GitLabUser> getMembersByProjectId(Long projectId){
        return gitLabUserRepository.findByProjectId(projectId);
    }
}
