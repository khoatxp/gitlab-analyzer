package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final MergeRequestCommentRepository noteRepository;
    private final ScoreService scoreService;
    private final GitLabService requestScopeGitLabService;
    private final AnalysisRunService analysisRunService;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository noteRepository, ScoreService scoreService, GitLabService requestScopeGitLabService, AnalysisRunService analysisRunService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.noteRepository = noteRepository;
        this.scoreService = scoreService;
        this.requestScopeGitLabService = requestScopeGitLabService;
        this.analysisRunService = analysisRunService;
    }

    public void saveMergeRequestInfo(AnalysisRun analysisRun, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var gitLabMergeRequests = requestScopeGitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().block();

        Double progress;
        Double startOfProgressRange = AnalysisRun.Progress.AtStartOfImportingMergeRequests.getValue();
        Double endOfProgressRange = AnalysisRun.Progress.AtStartOfImportingCommits.getValue();

        for(int i=0; i< gitLabMergeRequestList.size();i++) {
            progress = startOfProgressRange + (endOfProgressRange-startOfProgressRange) * (i+1)/gitLabMergeRequestList.size();
            analysisRunService.updateProgress(analysisRun, "Importing "+ (i+1) +"/"+gitLabMergeRequestList.size() + " merge requests",progress,false);

            var gitLabMergeRequest = gitLabMergeRequestList.get(i);
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

    public List<MergeRequest> getMergeRequestsByProjectId(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return mergeRequestRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime);
    }

    public List<MergeRequest> getMergeRequestsByProjectIdAndGitManagementUserId(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        return mergeRequestRepository.findAllByGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime);
    }
}
