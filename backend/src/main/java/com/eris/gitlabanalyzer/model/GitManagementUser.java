package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "GitManagementUser")
@Table(name = "git_management_user")
public class GitManagementUser {
    @Id
    @SequenceGenerator(
            name = "git_management_user_sequence",
            sequenceName = "git_management_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "git_management_user_sequence"
    )
    @Column(
            name = "git_management_user_id"
    )
    private Long id;

    @Column(
            name = "username",
            nullable = false

    )
    private String username;

    @Column(
            name = "name",
            nullable = false

    )
    private String name;

    @Column(
            name = "score",
            nullable = false

    )
    private float score;

    @OneToMany(
            mappedBy = "gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitMapping> commitMappings = new ArrayList<>();

    @OneToMany(
            mappedBy ="gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(
            mappedBy = "gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MergeRequest> mergeRequests = new ArrayList<>();

    @OneToMany(
            mappedBy = "gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitComment> commitComments = new ArrayList<>();

    @OneToMany(
            mappedBy = "gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<IssueComment> issueComments = new ArrayList<>();

    @OneToMany(
            mappedBy = "gitManagementUser",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MergeRequestComment> mergeRequestComments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "member",
            joinColumns = @JoinColumn(name = "git_management_user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "server_id",
            nullable = false,
            referencedColumnName = "server_id")
    private Server server;

    public GitManagementUser(){}

    public GitManagementUser(String username, String name, Server server) {
        this.username = username;
        this.name = name;
        this.server = server;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public List<CommitMapping> getCommitMappings() {
        return commitMappings;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public List<MergeRequest> getMergeRequests() {
        return mergeRequests;
    }

    public List<CommitComment> getCommitComments() {
        return commitComments;
    }

    public List<IssueComment> getIssueComments() {
        return issueComments;
    }

    public List<MergeRequestComment> getMergeRequestComments() {
        return mergeRequestComments;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Server getServer() {
        return server;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addProject(Project project) {
        if (!this.projects.contains(project)) {
            this.projects.add(project);
            project.getGitManagementUsers().add(this);
        }
    }


    public void addCommitMapping(CommitMapping commitMapping) {
        if (!this.commitMappings.contains(commitMapping)) {
            this.commitMappings.add(commitMapping);
            commitMapping.setGitManagementUser(this);
        }
    }

    public void addCommit(Commit commit) {
        if (!this.commits.contains(commit)) {
            this.commits.add(commit);
            commit.setGitManagementUser(this);
        }
    }

    public void addMergeRequest(MergeRequest mergeRequest) {
        if (!this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.add(mergeRequest);
            mergeRequest.setGitManagementUser(this);
        }
    }

    public void addCommitComment(CommitComment commitComment) {
        if (!this.commitComments.contains(commitComment)) {
            this.commitComments.add(commitComment);
            commitComment.setMember(this);
        }
    }

    public void addMergeRequestComment(MergeRequestComment mergeRequestComment) {
        if (!this.mergeRequestComments.contains(mergeRequestComment)) {
            this.mergeRequestComments.add(mergeRequestComment);
            mergeRequestComment.setMember(this);
        }
    }

    public void addIssueComment(IssueComment issueComment) {
        if (!this.issueComments.contains(issueComment)) {
            this.issueComments.add(issueComment);
            issueComment.setMember(this);
        }
    }

    @Override
    public String toString() {
        return "GitManagementUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
