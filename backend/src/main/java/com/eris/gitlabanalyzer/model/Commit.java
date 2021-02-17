package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Commit")
@Table(name = "commit")
public class Commit {
    @Id
    @SequenceGenerator(
            name = "commit_sequence",
            sequenceName = "commit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_sequence"
    )
    @Column(
            name = "commit_id"
    )
    private Long id;

    @Column(
            name = "sha",
            nullable = false
    )
    private String sha;

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
            name = "author_email",
            nullable = false

    )
    private String authorEmail;

    @Column(
            name = "committer_name",
            nullable = false

    )
    private String committerName;

    @Column(
            name = "committer_email",
            nullable = false

    )
    private String committerEmail;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "commit_id",
            nullable = true,
            referencedColumnName = "commit_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_commit_id_fk"
            )
    )
    private CommitMapping commitMapping;

    @OneToMany(
            mappedBy = "commit",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitComment> commitComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "gitlab_user_id",
            nullable = false,
            referencedColumnName = "gitlab_user_id")
    private GitLabUser gitLabUser;

    @ManyToOne
    @JoinColumn(
            name = "merge_request_id",
            nullable = true,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    public Commit() {
    }

    public Commit(String sha, String title, String authorName, String authorEmail, String committerName, String committerEmail, String committedDate, String createdAt, String webUrl, Project project, GitLabUser gitLabUser, MergeRequest mergeRequest) {
        this.sha = sha;
        this.title = title;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.committerName = committerName;
        this.committerEmail = committerEmail;
        this.committedDate = committedDate;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitLabUser = gitLabUser;
        this.mergeRequest = mergeRequest;
    }

    public CommitMapping getCommitMapping() {
        return commitMapping;
    }

    public void setCommitMapping(CommitMapping commitMapping) {
        this.commitMapping = commitMapping;
    }

    public Long getId() {
        return id;
    }

    public String getSha() {
        return sha;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
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

    public void setProject(Project project) {
        this.project = project;
    }

    public void setGitLabUser(GitLabUser gitLabUser) {
        this.gitLabUser = gitLabUser;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    public void addCommitComment(CommitComment commitComment) {
        if (!this.commitComments.contains(commitComment)) {
            this.commitComments.add(commitComment);
            commitComment.setCommit(this);
        }
    }

    public void removeCommitComment(CommitComment commitComment) {
        if (this.commitComments.contains(commitComment)) {
            this.commitComments.remove(commitComment);
            commitComment.setCommit(null);
        }
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", sha='" + sha + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", committerName='" + committerName + '\'' +
                ", committerEmail='" + committerEmail + '\'' +
                ", committedDate='" + committedDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
