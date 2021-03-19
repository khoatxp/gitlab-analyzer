package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Note")
@Table(name = "note")
@lombok.Getter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class Note {
    @Id
    @SequenceGenerator(
            name = "note_sequence",
            sequenceName = "note_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "note_sequence"
    )
    @Column(
            name = "note_id"
    )
    private Long id;

    @Column(
            name = "body",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String body;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "gitlab_note_id",
            nullable = false
    )
    private Long gitLabNoteId;

    @Column(
            name = "project_id",
            nullable = false
    )
    private Long projectId;

    @Column(
            name = "own",
            nullable = false
    )
    private boolean own;

    @Column(
            name = "parent_iid",
            nullable = false
    )
    private int parentIid;

    @Column(
            name = "parent_web_url",
            nullable = false
    )
    private String parentWebUrl;

    @Column(
            name = "noteable_type",
            nullable= false
    )
    private String noteableType;
}
