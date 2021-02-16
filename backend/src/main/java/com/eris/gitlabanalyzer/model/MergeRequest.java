package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "MergeRequest")
@Table(name = "merge_request")
public class MergeRequest {

    @Id
    @SequenceGenerator(
            name = "merge_request_sequence",
            sequenceName = "merge_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "merge_request_sequence"
    )
    @Column(
            name = "merge_request_id"
    )
    private Long id;

    @Column(
            name = "merge_request_iid"
    )
    private Long iid;

    @Column(
            name = "author_name",
            nullable = false
    )
    private String authorName;

    @Column(
            name = "author_username",
            nullable = false
    )
    private String authorUsername;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @Column(
            name = "description",
            columnDefinition="TEXT",
            nullable = false
    )
    private String description;

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
            referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            nullable = false,
            referencedColumnName = "member_id")
    private Member member;

    @OneToMany(
            mappedBy = "mergeRequest",
            orphanRemoval = false,
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(
            mappedBy = "mergeRequest",
            orphanRemoval = false,
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<MergeRequestComment> mergeRequestComments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Project getProject() {
        return project;
    }

    public Member getMember() {
        return member;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MergeRequest() {
    }

    public MergeRequest(Long iid, String authorName, String authorUsername, String title, String description, String createdAt, String webUrl, Project project, Member member) {
        this.iid = iid;
        this.authorName = authorName;
        this.authorUsername = authorUsername;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.member = member;
    }

    public void addCommit(Commit commit) {
        if (!this.commits.contains(commit)) {
            this.commits.add(commit);
            commit.setMergeRequest(this);
        }
    }

    public void removeCommit(Commit commit) {
        if (this.commits.contains(commit)) {
            this.commits.remove(commit);
            commit.setMergeRequest(null);
        }
    }

    public void addMergeRequestComment(MergeRequestComment mergeRequestComment) {
        if (!this.mergeRequestComments.contains(mergeRequestComment)) {
            this.mergeRequestComments.add(mergeRequestComment);
            mergeRequestComment.setMergeRequest(this);
        }
    }

    public void removeMergeRequestComment(MergeRequestComment mergeRequestComment) {
        if (this.mergeRequestComments.contains(mergeRequestComment)) {
            this.mergeRequestComments.remove(mergeRequestComment);
            mergeRequestComment.setMergeRequest(null);
        }
    }

    @Override
    public String toString() {
        return "MergeRequest{" +
                "id=" + id +
                ", iid=" + iid +
                ", authorName='" + authorName + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
