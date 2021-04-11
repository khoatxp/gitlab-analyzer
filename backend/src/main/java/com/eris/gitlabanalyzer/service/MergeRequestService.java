package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<MergeRequest> saveMergeRequestInfo(AnalysisRun analysisRun, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var gitLabMergeRequests = requestScopeGitLabService.getMergeRequests(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabMergeRequestList = gitLabMergeRequests.collectList().blockOptional().orElse(new ArrayList<>());

        Double progress;
        Double startOfProgressRange = AnalysisRun.Progress.AtStartOfImportingMergeRequests.getValue();
        Double endOfProgressRange = AnalysisRun.Progress.AtStartOfImportingCommits.getValue();

        List<MergeRequest> mergeRequests = new ArrayList<>();
        for(int i=0; i< gitLabMergeRequestList.size();i++) {
            progress = startOfProgressRange + (endOfProgressRange-startOfProgressRange) * (i+1)/gitLabMergeRequestList.size();
            analysisRunService.updateProgress(analysisRun, "Importing "+ (i+1) +"/"+gitLabMergeRequestList.size() + " merge requests",progress,false);

            var gitLabMergeRequest = gitLabMergeRequestList.get(i);
            GitManagementUser gitManagementUser = gitManagementUserRepository
                    .findByGitLabUserIdAndServerId(gitLabMergeRequest.getAuthor().getId(), project.getServer().getId())
                    .orElse(new GitManagementUser(
                            gitLabMergeRequest.getAuthor().getId(),
                            gitLabMergeRequest.getAuthor().getUsername(),
                            gitLabMergeRequest.getAuthor().getName(),
                            project.getServer())
                    );
            MergeRequest mergeRequest = mergeRequestRepository
                    .findByIidAndProjectId(gitLabMergeRequest.getIid(), project.getId())
                    .orElse(new MergeRequest(
                            gitLabMergeRequest.getIid(),
                            gitLabMergeRequest.getAuthor().getUsername(),
                            gitLabMergeRequest.getTitle(),
                            gitLabMergeRequest.getCreatedAt(),
                            gitLabMergeRequest.getMergedAt(),
                            gitLabMergeRequest.getWebUrl(),
                            project,
                            gitManagementUser
                    ));
            mergeRequest = mergeRequestRepository.save(mergeRequest);
            saveMergeRequestComments(project, mergeRequest);
            scoreService.saveMergeDiffMetrics(mergeRequest);
            mergeRequests.add(mergeRequest);
        }
        return mergeRequests;
    }

    public void saveMergeRequestComments(Project project, MergeRequest mergeRequest) {

        var gitLabMergeRequestComments = requestScopeGitLabService.getMergeRequestNotes(project.getGitLabProjectId(), mergeRequest.getIid());
        var gitLabMergeRequestCommentList = gitLabMergeRequestComments.collectList().block();

        Objects.requireNonNull(gitLabMergeRequestCommentList).parallelStream().forEach(gitLabNote -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository
                    .findByGitLabUserIdAndServerId(gitLabNote.getAuthor().getId(), project.getServer().getId())
                    .orElse(new GitManagementUser(
                            gitLabNote.getAuthor().getId(),
                            gitLabNote.getAuthor().getUsername(),
                            gitLabNote.getAuthor().getName(),
                            project.getServer())
                    );
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

    public List<MergeRequest> getMergeRequestsWhereGitManagementUserHasCommitsIn(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime){
        List<MergeRequest> ownerMergeRequests = mergeRequestRepository.findAllByGitManagementUserIdAndDateRange(projectId,gitManagementUserId, startDateTime, endDateTime);
        List<MergeRequest> notOwnerSharedMergeRequests = mergeRequestRepository.findParticipantSharedMergeRequests(projectId, gitManagementUserId, startDateTime, endDateTime);
        return Stream.of(ownerMergeRequests , notOwnerSharedMergeRequests).flatMap(Collection::stream)
                .sorted(Comparator.comparing(MergeRequest::getMergedAt))
                .collect(Collectors.toList());
    }
}
