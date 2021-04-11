package com.eris.gitlabanalyzer.model;

import lombok.Getter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Commit")
@Table(name = "commit")
@Getter
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
            nullable = false,
            columnDefinition="TEXT"
    )
    private String title;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @Column(
            name = "author_username"
    )
    private String authorUsername;

    @Column(
            name = "author_email",
            nullable = false

    )
    private String authorEmail;


    @Column(
            name = "created_at",
            nullable = false

    )
    private OffsetDateTime createdAt;

    @Column(
            name = "merged_at"

    )
    private OffsetDateTime mergedAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

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
            name = "merge_request_id",
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    public Commit() {
    }

    public Commit(String sha, String title, String authorName, String authorEmail, OffsetDateTime createdAt, String webUrl, Project project) {
        this.sha = sha;
        this.title = title;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
        this.mergedAt = mergeRequest.getMergedAt();
    }

    public void addCommitComment(CommitComment commitComment) {
        if (!this.commitComments.contains(commitComment)) {
            this.commitComments.add(commitComment);
            commitComment.setCommit(this);
        }
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", sha='" + sha + '\'' +
                ", title='" + title + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
