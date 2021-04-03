package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.MergeRequestComment;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final ProjectRepository projectRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final MergeRequestCommentRepository mergeRequestCommentRepository;
    private final ScoreService scoreService;
    private final GitLabService requestScopeGitLabService;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository mergeRequestCommentRepository, ScoreService scoreService, GitLabService requestScopeGitLabService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.mergeRequestCommentRepository = mergeRequestCommentRepository;
        this.scoreService = scoreService;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    public void saveMergeRequestInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {

        var gitLabMergeRequests = requestScopeGitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        gitLabMergeRequestList.forEach(gitLabMergeRequest -> {
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabMergeRequest.getAuthor().getId(), project.getServer().getId());
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

        var gitLabMergeRequestComments = requestScopeGitLabService.getMergeRequestNotes(project.getGitLabProjectId(), mergeRequest.getIid());
        var gitLabMergeRequestCommentList = gitLabMergeRequestComments.collectList().block();

        gitLabMergeRequestCommentList.parallelStream().forEach(gitLabMergeRequestComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabMergeRequestComment.getAuthor().getId(),project.getServer().getId());
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
        return mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
    }

    public List<MergeRequest> getMergeRequestsByProjectIdAndGitManagementUserId(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        return mergeRequestRepository.findAllByGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime);
    }

    public List<MergeRequest> getMergeRequestsWhereGitManagementUserHasCommitsIn(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<MergeRequest> ownerMergeRequests = mergeRequestRepository.findAllByGitManagementUserIdAndDateRange(projectId,gitManagementUserId, startDateTime, endDateTime);
        List<MergeRequest> notOwnerSharedMergeRequests = mergeRequestRepository.findParticipantSharedMergeRequests(projectId, gitManagementUserId, startDateTime, endDateTime);
        return Stream.of(ownerMergeRequests , notOwnerSharedMergeRequests).flatMap(Collection::stream)
                .sorted(Comparator.comparing(MergeRequest::getMergedAt))
                .collect(Collectors.toList());
    }
}
