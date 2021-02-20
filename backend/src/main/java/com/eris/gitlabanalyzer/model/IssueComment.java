package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

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
            name = "issue_comment_iid",
            nullable = false
    )
    private Long iid;

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
            name = "web_Url",
            nullable = false

    )
    private String webUrl;

    @Column(
            name = "created_at",
            nullable = false
    )
    private String createdAt;

    public IssueComment() {
    }

    public IssueComment(Long iid, GitManagementUser gitManagementUser, Issue issue, String webUrl, String createdAt) {
        this.iid = iid;
        this.gitManagementUser = gitManagementUser;
        this.issue = issue;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public GitManagementUser getGitLabUser() {
        return gitManagementUser;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getCreatedAt() {
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
                ", iid=" + iid +
                ", gitLabUser=" + gitManagementUser +
                ", issue=" + issue +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
