package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class MergeRequestService {
    private final MergeRequestRepository mergeRequestRepository;
    private final ProjectRepository projectRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final MergeRequestCommentRepository mergeRequestCommentRepository;
    private final ScoreService scoreService;
    private final GitLabService requestScopeGitLabService;
    private final AnalysisRunService analysisRunService;

    public MergeRequestService(MergeRequestRepository mergeRequestRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, MergeRequestCommentRepository mergeRequestCommentRepository, ScoreService scoreService, GitLabService requestScopeGitLabService, AnalysisRunService analysisRunService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.mergeRequestCommentRepository = mergeRequestCommentRepository;
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
}
