package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity(name = "Project")
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="project_id")
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
    private List<Member> members = new ArrayList<>();


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
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MergeRequest> mergeRequests = new ArrayList<>();

    public Project() {
    }

    public Project(Long gitLabProjectId, String name,String nameWithNamespace, String webUrl, Server server) {
        this.gitLabProjectId = gitLabProjectId;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.webUrl = webUrl;
        this.server = server;
    }

    public List<Member> getMembers() {
        return members;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public String getWebUrl() {
        return webUrl;
    }


    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.getProjects().add(this);
        }
    }

    public void removeMember(Member member) {
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.getProjects().remove(this);
        }
    }

    public void addCommit(Commit commit) {
        if (!this.commits.contains(commit)) {
            this.commits.add(commit);
            commit.setProject(this);
        }
    }

    public void removeCommit(Commit commit) {
        if (this.commits.contains(commit)) {
            this.commits.remove(commit);
            commit.setProject(null);
        }
    }

    public void addMergeRequest(MergeRequest mergeRequest) {
        if (!this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.add(mergeRequest);
            mergeRequest.setProject(this);
        }
    }

    public void removeMergeRequest(MergeRequest mergeRequest) {
        if (this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.remove(mergeRequest);
            mergeRequest.setProject(null);
        }
    }


}
