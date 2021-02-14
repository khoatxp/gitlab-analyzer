package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CommitComment")
@Table(name = "commit_comment")
public class CommitComment {

    @Id
    @SequenceGenerator(
            name = "commit_comment_sequence",
            sequenceName = "commit_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_comment_sequence"
    )
    @Column(
            name = "commit_comment_id"
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
            name = "commit_id",
            nullable = false,
            referencedColumnName = "commit_id")
    private Commit commit;

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

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Member getMember() {
        return member;
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

    public void setMember(Member member) {
        this.member = member;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public CommitComment() {
    }

    public CommitComment(Long id, String authorName, Member member, String body, String webUrl, String createdAt) {
        this.id = id;
        this.authorName = authorName;
        this.member = member;
        this.body = body;
        this.webUrl = webUrl;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", authorName='" + authorName + '\'' +
                ", member=" + member +
                ", body='" + body + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
