package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MergeRequestService {
    MergeRequestRepository mergeRequestRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    MergeRequestCommentRepository noteRepository;
    ScoreService scoreService;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository noteRepository, ScoreService scoreService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.noteRepository = noteRepository;
        this.scoreService = scoreService;
    }

    public void saveMergeRequestInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        var gitLabMergeRequests = gitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        Objects.requireNonNull(gitLabMergeRequestList).forEach(gitLabMergeRequest -> {
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

        Objects.requireNonNull(gitLabMergeRequestCommentList).parallelStream().forEach(gitLabNote -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabNote.getAuthor().getId(), serverUrl);
            Optional<Note> note = noteRepository.findByGitLabNoteIdAndProjectId(gitLabNote.getId(), project.getId());
            if(note.isEmpty()){
                boolean isOwn = gitLabNote.getAuthor().getId() == mergeRequest.getGitManagementUser().getGitLabUserId();
                noteRepository.save( new Note(
                        gitLabNote.getId(),
                        gitLabNote.getBody(),
                        gitManagementUser,
                        gitLabNote.getCreatedAt(),
                        project.getId(),
                        isOwn,
                        mergeRequest.getIid(),
                        mergeRequest.getWebUrl()
                ));
            }
        });
    }

    public List<MergeRequest> getMergeRequestsByProjectId(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO ensure user has permissions for project
        return mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
    }
}
