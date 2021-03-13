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
    IssueRepository issueRepository;
    IssueCommentRepository issueCommentRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public IssueService(IssueRepository issueRepository, IssueCommentRepository issueCommentRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public void saveIssueInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
        var gitLabIssues = gitLabService.getIssues(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabIssueList = gitLabIssues.collectList().block();

        gitLabIssueList.forEach(gitLabIssue -> {
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabIssue.getAuthor().getId(), serverUrl);
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
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
        var gitLabIssueComments = gitLabService.getIssueNotes(project.getGitLabProjectId(), issue.getIid());
        var gitLabIssueCommentList = gitLabIssueComments.collectList().block();

        gitLabIssueCommentList.parallelStream().forEach(gitLabIssueComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabIssueComment.getAuthor().getId(),serverUrl);
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
