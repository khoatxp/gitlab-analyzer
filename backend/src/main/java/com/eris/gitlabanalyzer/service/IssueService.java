package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.IssueRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueCommentRepository issueCommentRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final GitLabService requestScopeGitLabService;
    private final AnalysisRunService analysisRunService;

    public IssueService(IssueRepository issueRepository, IssueCommentRepository issueCommentRepository, GitManagementUserRepository gitManagementUserRepository, GitLabService requestScopeGitLabService, AnalysisRunService analysisRunService) {
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.requestScopeGitLabService = requestScopeGitLabService;
        this.analysisRunService = analysisRunService;
    }

    public void saveIssueInfo(AnalysisRun analysisRun, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var gitLabIssues = requestScopeGitLabService.getIssues(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabIssueList = gitLabIssues.collectList().block();

        Double progress;
        Double startOfProgressRange = AnalysisRun.Progress.AtStartOfImportingIssues.getValue();
        Double endOfProgressRange = AnalysisRun.Progress.Done.getValue()-1;

        for(int i=0; i<gitLabIssueList.size(); i++){
            progress = startOfProgressRange + (endOfProgressRange-startOfProgressRange) * (i+1)/gitLabIssueList.size();
            analysisRunService.updateProgress(analysisRun, "Importing "+ (i+1) +"/"+gitLabIssueList.size() + " issues",progress, false);

            var gitLabIssue = gitLabIssueList.get(i);
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabIssue.getAuthor().getId(), project.getServer().getId());
            Issue issue = issueRepository.findByIidAndProjectId(gitLabIssue.getIid(),project.getId());
            if(issue == null){
                issue = new Issue(
                        gitLabIssue.getIid(),
                        gitLabIssue.getTitle(),
                        gitLabIssue.getAuthor().getName(),
                        gitLabIssue.getCreatedAt(),
                        gitLabIssue.getWebUrl(),
                        project,
                        gitManagementUser
                );
            }
            issue = issueRepository.save(issue);
            saveIssueComments(project, issue);
        }
    }

    public void saveIssueComments(Project project, Issue issue) {
        var gitLabIssueComments = requestScopeGitLabService.getIssueNotes(project.getGitLabProjectId(), issue.getIid());
        var gitLabIssueCommentList = gitLabIssueComments.collectList().block();

        Objects.requireNonNull(gitLabIssueCommentList).parallelStream().forEach(gitLabNote -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabNote.getAuthor().getId(), project.getServer().getId());
            Optional<Note> note = issueCommentRepository.findByGitLabNoteIdAndProjectId(gitLabNote.getId(), project.getId());
            if (note.isEmpty() && !gitLabNote.isSystem()) {
                boolean isOwn = gitLabNote.getAuthor().getId().equals(issue.getGitManagementUser().getGitLabUserId());
                issueCommentRepository.save(new Note(
                        gitLabNote.getId(),
                        gitLabNote.getBody(),
                        gitManagementUser,
                        gitLabNote.getCreatedAt(),
                        null,
                        project.getId(),
                        isOwn,
                        issue.getIid(),
                        issue.getWebUrl(),
                        gitLabNote.getNoteableType()
                ));
            }
        });
    }


}
