package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
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
    GitManagementUserRepository gitManagementUserRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MergeRequestService(GitLabService gitLabService, MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.gitLabService = gitLabService;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public void saveMergeRequestInfo(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabMergeRequests = gitLabService.getMergeRequests(gitLabProjectId, startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        if (gitLabMergeRequestList != null && !gitLabMergeRequestList.isEmpty()) {
            gitLabMergeRequestList.forEach(gitLabMergeRequest -> {
                        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(gitLabMergeRequest.getAuthor().getUsername(), serverUrl);
                        MergeRequest mergeRequest = mergeRequestRepository.findByIidAndProjectId(gitLabMergeRequest.getIid(),project.getId());
                        if(mergeRequest == null){
                            mergeRequest = new MergeRequest(
                                    gitLabMergeRequest.getIid(),
                                    gitLabMergeRequest.getAuthor().getUsername(),
                                    gitLabMergeRequest.getTitle(),
                                    gitLabMergeRequest.getCreatedAt(),
                                    gitLabMergeRequest.getWebUrl(),
                                    project,
                                    gitManagementUser
                            );
                        }
                        mergeRequestRepository.save(mergeRequest);
                    }
            );
        }
    }

}
