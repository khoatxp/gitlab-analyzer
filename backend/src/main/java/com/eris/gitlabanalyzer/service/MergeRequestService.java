package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.GitManagementUser;
import com.eris.gitlabanalyzer.model.MergeRequest;
import com.eris.gitlabanalyzer.model.Note;
import com.eris.gitlabanalyzer.model.Project;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final MergeRequestCommentRepository noteRepository;
    private final ScoreService scoreService;
    private final GitLabService requestScopeGitLabService;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository noteRepository, ScoreService scoreService, GitLabService requestScopeGitLabService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.noteRepository = noteRepository;
        this.scoreService = scoreService;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    public void saveMergeRequestInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {

        var gitLabMergeRequests = requestScopeGitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        Objects.requireNonNull(gitLabMergeRequestList).forEach(gitLabMergeRequest -> {
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabMergeRequest.getAuthor().getId(), project.getServer().getId());
                    MergeRequest mergeRequest = mergeRequestRepository.findByIidAndProjectId(gitLabMergeRequest.getIid(), project.getId());
                    if (mergeRequest == null) {
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

    public void saveMergeRequestComments(Project project, MergeRequest mergeRequest) {

        var gitLabMergeRequestComments = requestScopeGitLabService.getMergeRequestNotes(project.getGitLabProjectId(), mergeRequest.getIid());
        var gitLabMergeRequestCommentList = gitLabMergeRequestComments.collectList().block();

        Objects.requireNonNull(gitLabMergeRequestCommentList).parallelStream().forEach(gitLabNote -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabNote.getAuthor().getId(), project.getServer().getId());
            Optional<Note> note = noteRepository.findByGitLabNoteIdAndProjectId(gitLabNote.getId(), project.getId());
            if (note.isEmpty() && !gitLabNote.isSystem()) {
                boolean isOwn = gitLabNote.getAuthor().getId().equals(mergeRequest.getGitManagementUser().getGitLabUserId());
                noteRepository.save(new Note(
                        gitLabNote.getId(),
                        gitLabNote.getBody(),
                        gitManagementUser,
                        gitLabNote.getCreatedAt(),
                        mergeRequest.getMergedAt(),
                        project.getId(),
                        isOwn,
                        mergeRequest.getIid(),
                        mergeRequest.getWebUrl(),
                        gitLabNote.getNoteableType()
                ));
            }
        });
    }
}
