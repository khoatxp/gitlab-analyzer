package com.eris.gitlabanalyzer.model;

import javax.persistence. *;

@Entity(name = "Issue")
@Table(name = "issue")
public class Issue {
    @Id
    @Column(
            name = "id"
    )
    private Long id;

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

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "issue_project_fk"
            )
    )
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "user_name",
            nullable = false,
            referencedColumnName = "user_name",
            foreignKey = @ForeignKey(
                    name = "issue_member_fk"
            )
    )
    private Member member;

    public Issue() {
    }

    public Issue(Long id, String title, String authorName, String createdAt, String webUrl, Project project, Member member) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.member = member;
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

    public Member getMember() {
        return member;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(Member member) {
        this.member = member;
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
                ", member=" + member +
                '}';
    }
}
