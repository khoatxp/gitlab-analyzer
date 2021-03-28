package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.MergeRequestComment;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.viewmodel.ScoreDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class MergeRequestService {
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    MergeRequestCommentRepository mergeRequestCommentRepository;
    ScoreService scoreService;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository mergeRequestCommentRepository, ScoreService scoreService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.mergeRequestCommentRepository = mergeRequestCommentRepository;
        this.scoreService = scoreService;
    }

    public void saveMergeRequestInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

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
                                gitLabMergeRequest.getMergedAt(),
                                gitLabMergeRequest.getWebUrl(),
                                project,
                                gitManagementUser
                        );
                    }
                    mergeRequest = mergeRequestRepository.save(mergeRequest);
                    saveMergeRequestComments(project, mergeRequest);
                    scoreService.saveMergeDiffMetrics(mergeRequest);
                }
        );
    }

    public void saveMergeRequestComments (Project project, MergeRequest mergeRequest){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

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

    public List<MergeRequest> getMergeRequestsByProjectId(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO ensure user has permissions for project
        return mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
    }

    public List<MergeRequest> getMergeRequestsByProjectIdAndGitManagementUserId(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        return mergeRequestRepository.findAllByGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime);
    }
}
