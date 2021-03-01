package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.IssueRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class IssueService {
    GitLabService gitLabService;
    IssueRepository issueRepository;
    IssueCommentRepository issueCommentRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    public IssueService(GitLabService gitLabService, IssueRepository issueRepository, IssueCommentRepository issueCommentRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.gitLabService = gitLabService;
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public void saveIssueInfo(Long gitLabProjectId, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
        Project project = projectRepository.findByGitlabProjectIdAndServerUrl(gitLabProjectId, serverUrl);

        var gitLabIssues = gitLabService.getIssues(gitLabProjectId, startDateTime, endDateTime);
        var gitLabIssueList = gitLabIssues.collectList().block();

        if (gitLabIssueList != null && !gitLabIssueList.isEmpty()) {
            gitLabIssueList.forEach(gitLabIssue -> {
                        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(gitLabIssue.getAuthor().getUsername(), serverUrl);
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
                        issueRepository.save(issue);
                        saveIssueComments(project, gitLabIssue.getIid());
                    }
            );
        }
    }

    public void saveIssueComments (Project project, Long issueIid){
        Issue issue = issueRepository.findByIidAndProjectId(issueIid, project.getId());
        var gitLabIssueComments = gitLabService.getIssueNotes(project.getGitLabProjectId(), issue.getIid());
        var gitLabIssueCommentList = gitLabIssueComments.collectList().block();
        if (gitLabIssueCommentList != null && !gitLabIssueCommentList.isEmpty()) {
            gitLabIssueCommentList.forEach(gitLabIssueComment -> {
                GitManagementUser gitManagementUser = gitManagementUserRepository.findByUserNameAndServerUrl(gitLabIssueComment.getAuthor().getUsername(),serverUrl);
                IssueComment issueComment = issueCommentRepository.findByIssueNoteIdAndIssueId(gitLabIssueComment.getId(),issue.getIid());
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
    
    
}
