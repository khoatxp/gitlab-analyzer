package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitLabUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitLabUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class MergeRequestService {
    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitLabUserRepository gitLabUserRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MergeRequestService(GitLabService gitLabService, MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitLabUserRepository gitLabUserRepository) {
        this.gitLabService = gitLabService;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitLabUserRepository = gitLabUserRepository;
    }

    public void saveMergeRequestInfo(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime){
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabMergeRequests = gitLabService.getMergeRequests(gitLabProjectId, startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        if (gitLabMergeRequestList != null && !gitLabMergeRequestList.isEmpty()){
            gitLabMergeRequestList.forEach(gitLabMergeRequest -> {
                    GitLabUser gitLabUser = gitLabUserRepository.findByUserNameAndServerUrl(gitLabMergeRequest.getUsername(),serverUrl);
                    MergeRequest mergeRequest = new MergeRequest(
                            gitLabMergeRequest.getIid(),
                            gitLabMergeRequest.getUsername(),
                            gitLabMergeRequest.getTitle(),
                            gitLabMergeRequest.getCreatedAt(),
                            gitLabMergeRequest.getWebUrl(),
                            project,
                            gitLabUser
                    );
                    mergeRequestRepository.save(mergeRequest);
                }
            );
        }

    }
}
