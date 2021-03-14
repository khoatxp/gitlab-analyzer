package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.MergeRequestComment;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class MergeRequestService {
    GitLabService gitLabService;
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    MergeRequestCommentRepository mergeRequestCommentRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    public MergeRequestService(GitLabService gitLabService, MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository mergeRequestCommentRepository) {
        this.gitLabService = gitLabService;
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.mergeRequestCommentRepository = mergeRequestCommentRepository;
    }

    public void saveMergeRequestInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var gitLabMergeRequests = gitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        gitLabMergeRequestList.forEach(gitLabMergeRequest -> {
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabMergeRequest.getAuthor().getId(), serverUrl);
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
                    mergeRequest = mergeRequestRepository.save(mergeRequest);
                    saveMergeRequestComments(project, mergeRequest);
                }
        );
    }

    public void saveMergeRequestComments (Project project, MergeRequest mergeRequest){
        var gitLabMergeRequestComments = gitLabService.getMergeRequestNotes(project.getGitLabProjectId(), mergeRequest.getIid());
        var gitLabMergeRequestCommentList = gitLabMergeRequestComments.collectList().block();

        gitLabMergeRequestCommentList.parallelStream().forEach(gitLabMergeRequestComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabMergeRequestComment.getAuthor().getId(),serverUrl);
            MergeRequestComment mergeRequestComment = mergeRequestCommentRepository.findByGitLabMergeRequestNoteIdAndMergeRequestId(gitLabMergeRequestComment.getId(),mergeRequest.getId());
            if(mergeRequestComment == null){
                mergeRequestComment = new MergeRequestComment(
                        gitLabMergeRequestComment.getId(),
                        gitLabMergeRequestComment.getBody(),
                        gitLabMergeRequestComment.getCreatedAt(),
                        gitManagementUser,
                        mergeRequest
                );
            }
            mergeRequestCommentRepository.save(mergeRequestComment);
        });
    }

}
