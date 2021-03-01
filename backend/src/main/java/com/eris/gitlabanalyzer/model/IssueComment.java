package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "IssueComment")
@Table(name = "issue_comment")
public class IssueComment {
    @Id
    @SequenceGenerator(
            name = "issue_comment_sequence",
            sequenceName = "issue_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "issue_comment_sequence"
    )
    @Column(
            name = "issue_comment_id"
    )
    private Long id;

    @Column(
            name = "gitlab_issue_note_id",
            nullable = false
    )
    private Long gitLabIssueNoteId;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    @ManyToOne
    @JoinColumn(
            name = "issue_id",
            nullable = false,
            referencedColumnName = "issue_id")
    private Issue issue;

    @Column(
            name = "body",
            nullable = false,
            columnDefinition = "TEXT"

    )
    private String body;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    public IssueComment() {
    }

    public IssueComment(Long gitLabIssueNoteId, String body, OffsetDateTime createdAt, GitManagementUser gitManagementUser, Issue issue) {
        this.gitLabIssueNoteId = gitLabIssueNoteId;
        this.gitManagementUser = gitManagementUser;
        this.issue = issue;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getgitLabIssueNoteId() {
        return gitLabIssueNoteId;
    }

    public GitManagementUser getGitManagementUser() {
        return gitManagementUser;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getbody() {
        return body;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setMember(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "IssueComment{" +
                "id=" + id +
                ", gitLabIssueNoteId=" + gitLabIssueNoteId +
                ", gitManagementUser=" + gitManagementUser +
                ", issue=" + issue +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
