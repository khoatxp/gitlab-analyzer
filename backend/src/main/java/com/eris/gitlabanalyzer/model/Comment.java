package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

@Entity(name = "Comment")
@Table(name = "comment")
public class Comment {

    @Id
    @Column(
            name = "id"
    )
    private String id;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @ManyToOne
    @JoinColumn(
            name = "user_name",
            nullable = false,
            referencedColumnName = "user_name",
            foreignKey = @ForeignKey(
                    name = "comment_member_fk"
            )
    )
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

    public String getId() {
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

    public Comment(String id, String authorName, Member member, String body, String webUrl, String createdAt) {
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
