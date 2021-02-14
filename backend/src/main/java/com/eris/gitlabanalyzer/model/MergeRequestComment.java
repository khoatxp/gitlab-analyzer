package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "MergeRequestComment")
@Table(name = "merge_request_comment")
public class MergeRequestComment {
    @Id
    @SequenceGenerator(
            name = "merge_request_comment_sequence",
            sequenceName = "merge_request_comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "merge_request_comment_sequence"
    )
    @Column(
            name = "merge_request_comment_id"
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
            name = "merge_request_id",
            nullable = false,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

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
}
