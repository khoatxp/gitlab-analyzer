package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Commit;
import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CommitService {

    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }

    public void saveCommitInfo(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

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