package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "MergeRequestComment")
@Table(name = "merge_request_comment")
public class MergeRequestComment {
    @Id
    @SequenceGenerator(
            name = "merge_request_comment_sequence",
            sequenceName = "merge_request_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "merge_request_comment_sequence"
    )
    @Column(
            name = "merge_request_comment_id"
    )
    private Long id;

    @Column(
            name = "merge_request_comment_iid",
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
            name = "merge_request_id",
            nullable = false,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @Column(
            name = "created_at",
            nullable = false
    )
    private String createdAt;

    public MergeRequestComment() {
    }

    public MergeRequestComment(Long iid, GitManagementUser gitManagementUser, MergeRequest mergeRequest, String webUrl, String createdAt) {
        this.iid = iid;
        this.gitManagementUser = gitManagementUser;
        this.mergeRequest = mergeRequest;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public GitManagementUser getMember() {
        return gitManagementUser;
    }

    public MergeRequest getMergeRequest() {
        return mergeRequest;
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

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    @Override
    public String toString() {
        return "MergeRequestComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", gitManagementUser=" + gitManagementUser +
                ", mergeRequest=" + mergeRequest +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
