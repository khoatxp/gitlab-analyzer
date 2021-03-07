package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CommitComment")
@Table(name = "commit_comment")
public class CommitComment {

    @Id
    @SequenceGenerator(
            name = "commit_comment_sequence",
            sequenceName = "commit_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_comment_sequence"
    )
    @Column(
            name = "commit_comment_id"
    )
    private Long id;

    @Column(
            name = "commit_comment_iid",
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
            name = "commit_id",
            nullable = false,
            referencedColumnName = "commit_id")
    private Commit commit;

    @Column(
            name = "web_Url",
            nullable = false

    )
    private String webUrl;

    @Column(
            name = "created_at",
            nullable = false

    )
    private OffsetDateTime createdAt;

    public Long getId() {
        return id;
    }

    public GitManagementUser getMember() {
        return gitManagementUser;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setMember(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public CommitComment() {
    }

    public CommitComment(Long iid, GitManagementUser gitManagementUser, Commit commit, String webUrl, OffsetDateTime createdAt) {
        this.iid = iid;
        this.gitManagementUser = gitManagementUser;
        this.commit = commit;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CommitComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", gitManagementUser=" + gitManagementUser +
                ", commit=" + commit +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
