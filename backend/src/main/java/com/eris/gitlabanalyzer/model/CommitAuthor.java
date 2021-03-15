package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "CommitAuthor")
@Table(name = "commit_author")
public class CommitAuthor {

    @Id
    @SequenceGenerator(
            name = "commit_author_sequence",
            sequenceName = "commit_author_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_author_sequence"
    )
    @Column(
            name = "commit_author_id"
    )
    private Long id;

    @Column(
            name = "author_name",
            nullable = false
    )
    private String authorName;

    @Column(
            name = "author_email",
            nullable = false
    )
    private String authorEmail;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_project_id_fk"
            )
    )
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_member_id_fk"
            )
    )
    private GitManagementUser gitManagementUser;


    public CommitAuthor() {
    }

    public CommitAuthor(String authorName, String authorEmail, Project project) {
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.project = project;
    }

    public GitManagementUser getGitManagementUser() {
        return gitManagementUser;
    }

    public void setGitManagementUser(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

}
