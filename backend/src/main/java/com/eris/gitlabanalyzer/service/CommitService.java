package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class CommitService {

    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(GitLabService gitLabService, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.gitLabService = gitLabService;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }

    public void saveCommitInfo(Long gitLabProjectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabCommits = gitLabService.getCommits(gitLabProjectId, startDateTime, endDateTime);
        var gitLabCommitList = gitLabCommits.collectList().block();

        if (gitLabCommitList != null && !gitLabCommitList.isEmpty()) {
            gitLabCommitList.forEach(gitLabCommit -> {
                        String authorName = gitLabCommit.getAuthorEmail();
                        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(authorName, serverUrl);
                        Commit commit = new Commit(
                                gitLabCommit.getSha(),
                                gitLabCommit.getTitle(),
                                authorName,
                                gitLabCommit.getAuthorEmail(),
                                authorName,
                                gitLabCommit.getAuthorEmail(),
                                gitLabCommit.getCreatedAt(),
                                gitLabCommit.getWebUrl(),
                                project,
                                gitManagementUser
                        );
                        project.addCommit(commit);
                        gitManagementUser.addCommit(commit);
                    }
            );
        }
    }

}