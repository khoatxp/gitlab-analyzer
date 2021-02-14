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
            name = "author_name",
            nullable = false
    )
    private String authorName;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

    @Column(
            name = "description",
            nullable = false
    )
    private String description;

    @Column(
            name = "created_at",
            nullable = false
    )
    private String created_at;

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

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
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

    public MergeRequest(Long id, String authorName, String title, String description, String created_at, String webUrl, Project project, Member member) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
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

    @Override
    public String toString() {
        return "MergeRequest{" +
                "id=" + id +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", project=" + project +
                ", member=" + member +
                '}';
    }
}
