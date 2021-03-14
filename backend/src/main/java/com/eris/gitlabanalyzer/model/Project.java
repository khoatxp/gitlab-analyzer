package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Project")
@Table(
        name = "project",
        uniqueConstraints={@UniqueConstraint(columnNames={"server_id", "gitlab_project_id"})}
)
public class Project {
    @Id
    @SequenceGenerator(
            name = "project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "project_sequence"
    )
    @Column(
            name = "project_id"
    )
    private Long id;

    @Column(
            name = "gitlab_project_id",
            nullable = false
    )
    private Long gitLabProjectId;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "name_with_namespace",
            nullable = false

    )
    private String nameWithNamespace;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();


    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MergeRequest> mergeRequests = new ArrayList<>();

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<UserProjectPermission> userProjectPermissions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "server_id",
            nullable = false,
            referencedColumnName = "server_id"
    )
    private Server server;

    @ManyToMany(mappedBy = "projects",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    private List<GitManagementUser> gitManagementUsers = new ArrayList<>();

    public Project() {
    }

    public Project(Long gitLabProjectId, String name, String nameWithNamespace, String webUrl, Server server) {
        this.gitLabProjectId = gitLabProjectId;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.webUrl = webUrl;
        this.server = server;
    }

    public Long getId() {
        return id;
    }

    public Long getGitLabProjectId() {
        return gitLabProjectId;
    }

    public String getName() {
        return name;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Commit> getCommits() {
        return commits;
    }

    public List<MergeRequest> getMergeRequests() {
        return mergeRequests;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public Server getServer() {
        return server;
    }

    public List<GitManagementUser> getGitManagementUsers() {
        return gitManagementUsers;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addGitManagementUser(GitManagementUser gitManagementUser) {
        if (!this.gitManagementUsers.contains(gitManagementUser)) {
            this.gitManagementUsers.add(gitManagementUser);
            gitManagementUser.getProjects().add(this);
        }
    }

    public void addCommit(Commit commit) {
        if (!this.commits.contains(commit)) {
            this.commits.add(commit);
            commit.setProject(this);
        }
    }

    public void addMergeRequest(MergeRequest mergeRequest) {
        if (!this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.add(mergeRequest);
            mergeRequest.setProject(this);
        }
    }

    public void addIssue(Issue issue) {
        if (!this.issues.contains(issue)) {
            this.issues.add(issue);
            issue.setProject(this);
        }
    }

    public void addUserProjectPermission(UserProjectPermission userProjectPermission) {
        if (!this.userProjectPermissions.contains(userProjectPermission)) {
            this.userProjectPermissions.add(userProjectPermission);
            userProjectPermission.setProject(this);
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", gitLabProjectId=" + gitLabProjectId +
                ", name='" + name + '\'' +
                ", nameWithNamespace='" + nameWithNamespace + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
