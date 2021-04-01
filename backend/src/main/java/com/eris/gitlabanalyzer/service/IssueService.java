package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.IssueRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueCommentRepository issueCommentRepository;
    private final ProjectRepository projectRepository;
    private final GitManagementUserRepository gitManagementUserRepository;
    private final GitLabService requestScopeGitLabService;;

    public IssueService(IssueRepository issueRepository, IssueCommentRepository issueCommentRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, GitLabService requestScopeGitLabService) {
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    public void saveIssueInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var gitLabIssues = requestScopeGitLabService.getIssues(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabIssueList = gitLabIssues.collectList().block();

        gitLabIssueList.forEach(gitLabIssue -> {
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
        );
    }

    public void saveIssueComments (Project project, Issue issue){
        var gitLabIssueComments = requestScopeGitLabService.getIssueNotes(project.getGitLabProjectId(), issue.getIid());
        var gitLabIssueCommentList = gitLabIssueComments.collectList().block();

        gitLabIssueCommentList.parallelStream().forEach(gitLabIssueComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerId(gitLabIssueComment.getAuthor().getId(),project.getServer().getId());
            IssueComment issueComment = issueCommentRepository.findByGitLabIssueNoteIdAndIssueId(gitLabIssueComment.getId(),issue.getIid());
            if(issueComment == null){
                issueComment = new IssueComment(
                        gitLabIssueComment.getId(),
                        gitLabIssueComment.getBody(),
                        gitLabIssueComment.getCreatedAt(),
                        gitManagementUser,
                        issue
                );
            }
            issueCommentRepository.save(issueComment);
        });

    }
    
    
}
