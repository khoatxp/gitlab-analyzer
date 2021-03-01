package com.eris.gitlabanalyzer.model;

import javax.persistence. *;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Issue")
@Table(name = "issue")
public class Issue {
    @Id
    @SequenceGenerator(
            name = "issue_sequence",
            sequenceName = "issue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "issue_sequence"
    )
    @Column(
            name = "issue_id"
    )
    private Long id;

    @Column(
            name = "issue_iid",
            nullable = false
    )
    private Long iid;

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
            name = "created_at",
            nullable = false

    )
    private OffsetDateTime createdAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @OneToMany(
            mappedBy = "issue",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<IssueComment> issueComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    public Issue() {
    }

    public Issue(Long iid, String title, String authorName, OffsetDateTime createdAt, String webUrl, Project project, GitManagementUser gitManagementUser) {
        this.iid = iid;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitManagementUser = gitManagementUser;
    }

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Project getProject() {
        return project;
    }

    public GitManagementUser getMember() {
        return gitManagementUser;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public void addIssueComment(IssueComment issueComment) {
        if (!this.issueComments.contains(issueComment)) {
            this.issueComments.add(issueComment);
            issueComment.setIssue(this);
        }
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", project=" + project +
                '}';
    }
}
