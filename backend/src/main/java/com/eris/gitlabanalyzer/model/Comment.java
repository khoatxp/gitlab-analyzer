package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

@Entity(name = "Comment")
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="comment_id")
    private Long id;

    @Column(
            name = "gitlab_comment_id",
            nullable = false
    )
    private Long gitLabCommentId;

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

    @Column(
            name = "body",
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

    public Comment() {
    }

    public Comment(Long id, String authorName, Member member, String body, String webUrl, String createdAt) {
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
