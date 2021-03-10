package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
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
            name = "gitlab_merge_request_note_id",
            nullable = false
    )
    private Long gitLabMergeRequestNoteId;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    @ManyToOne
    @JoinColumn(
            name = "merge_request_id",
            nullable = false,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    @Column(
            name = "body",
            nullable = false,
            columnDefinition="TEXT"

    )
    private String body;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    public MergeRequestComment() {
    }

    public MergeRequestComment(Long gitLabMergeRequestNoteId, String body, OffsetDateTime createdAt, GitManagementUser gitManagementUser, MergeRequest mergeRequest) {
        this.gitLabMergeRequestNoteId = gitLabMergeRequestNoteId;
        this.gitManagementUser = gitManagementUser;
        this.mergeRequest = mergeRequest;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getGitLabMergeRequestNoteId() {
        return gitLabMergeRequestNoteId;
    }

    public GitManagementUser getMember() {
        return gitManagementUser;
    }

    public MergeRequest getMergeRequest() {
        return mergeRequest;
    }

    public String getBody() {
        return body;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setMember(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    @Override
    public String toString() {
        return "MergeRequestComment{" +
                "id=" + id +
                ", gitLabMergeRequestNoteId=" + gitLabMergeRequestNoteId +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
