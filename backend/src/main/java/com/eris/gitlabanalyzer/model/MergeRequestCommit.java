package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

@Entity(name = "MergeRequestCommit")
@Table(name = "merge_request_commit")
public class MergeRequestCommit {
    @Id
    @Column(
            name = "id"
    )
    private String id;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @Column(
            name = "author_name",
            nullable = false
    )
    private String authorName;

    @Column(
            name = "committer_name",
            nullable = false

    )
    private String committerName;

    @Column(
            name = "committed_date",
            nullable = false

    )
    private String committedDate;

    @Column(
            name = "created_at",
            nullable = false

    )
    private String createdAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "mr_commit_mr_id_fk"
            )
    )
    private MergeRequest mergeRequest;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommittedDate() {
        return committedDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public MergeRequest getMergeRequest() {
        return mergeRequest;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    @Override
    public String toString() {
        return "MergeRequstCommit{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", committerName='" + committerName + '\'' +
                ", committedDate='" + committedDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", mergeRequest=" + mergeRequest +
                '}';
    }
}
