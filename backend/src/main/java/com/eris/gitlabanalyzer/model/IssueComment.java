package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "IssueComment")
@Table(name = "issue_comment")
public class IssueComment {
    @Id
    @SequenceGenerator(
            name = "issue_comment_sequence",
            sequenceName = "issue_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "issue_comment_sequence"
    )
    @Column(
            name = "issue_comment_id"
    )
    private Long id;

    @Column(
            name = "gitlab_iid",
            nullable = false
    )
    private Long iid;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @ManyToOne
    @JoinColumn(
            name = "member_id",
            nullable = false,
            referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "project_id")
    private Project project;

    @Column(
            name = "body",
            columnDefinition="TEXT",
            nullable = false

    )
    private String body;

    @Column(
            name = "web_Url",
            nullable = false

    )
    private String webUrl;

    @Column(
            name = "created_at",
            nullable = false
    )
    private String createdAt;

    public IssueComment() {
    }

    public IssueComment(Long iid, String authorName, Member member, Project project,
                        String body, String webUrl, String createdAt) {
        this.iid = iid;
        this.authorName = authorName;
        this.member = member;
        this.project = project;
        this.body = body;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "IssueComment{" +
                "id=" + id +
                ", iid=" + iid +
                ", authorName='" + authorName + '\'' +
                ", member=" + member +
                ", project=" + project +
                ", body='" + body + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
