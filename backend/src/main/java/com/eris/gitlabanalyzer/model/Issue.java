package com.eris.gitlabanalyzer.model;

import javax.persistence. *;

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
    private String createdAt;

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
            name = "gitlab_user_id",
            nullable = false,
            referencedColumnName = "gitlab_user_id")
    private GitLabUser gitLabUser;

    public Issue() {
    }

    public Issue(Long id, String title, String authorName, String createdAt, String webUrl, Project project, GitLabUser gitLabUser) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitLabUser = gitLabUser;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
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

    public GitLabUser getMember() {
        return gitLabUser;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(GitLabUser gitLabUser) {
        this.gitLabUser = gitLabUser;
    }

    public void addIssueComment(IssueComment issueComment) {
        if (!this.issueComments.contains(issueComment)) {
            this.issueComments.add(issueComment);
            issueComment.setIssue(this);
        }
    }

    public void removeIssueComment(IssueComment issueComment) {
        if (this.issueComments.contains(issueComment)) {
            this.issueComments.remove(issueComment);
            issueComment.setIssue(null);
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
