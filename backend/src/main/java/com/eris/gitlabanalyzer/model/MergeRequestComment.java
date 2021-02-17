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
            name = "gitlab_user_id",
            nullable = false,
            referencedColumnName = "gitlab_user_id")
    private GitLabUser gitLabUser;

    @ManyToOne
    @JoinColumn(
            name = "merge_request_id",
            nullable = false,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    @Column(
            name = "body",
            columnDefinition="TEXT",
            nullable = false

    )
    private String body;

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

    public MergeRequestComment(Long iid,GitLabUser gitLabUser, MergeRequest mergeRequest, String body,
                               String webUrl, String createdAt) {
        this.iid = iid;
        this.gitLabUser = gitLabUser;
        this.mergeRequest = mergeRequest;
        this.body = body;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public GitLabUser getMember() {
        return gitLabUser;
    }

    public MergeRequest getMergeRequest() {
        return mergeRequest;
    }

    public String getBody() {
        return body;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setMember(GitLabUser gitLabUser) {
        this.gitLabUser = gitLabUser;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    @Override
    public String toString() {
        return "MergeRequestComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", body='" + body + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
