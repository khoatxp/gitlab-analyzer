package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Member")
@Table(name = "member")
public class Member {
    @Id
    @SequenceGenerator(
            name = "member_sequence",
            sequenceName = "member_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "member_sequence"
    )
    @Column(
            name = "member_id"
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

    @ManyToMany
    @JoinTable(
            name = "participate_in",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "server_id",
            nullable = false,
            referencedColumnName = "server_id")
    private Server server;

    @OneToMany(
            mappedBy = "member",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitMapping> commitMappings = new ArrayList<>();

    @OneToMany(
            mappedBy = "member",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MergeRequest> mergeRequests = new ArrayList<>();

    @OneToMany(
            mappedBy = "commit",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitComment> commitComments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Server getServer() {
        return server;
    }

    public List<CommitMapping> getCommitMappings() {
        return commitMappings;
    }

    public List<MergeRequest> getMergeRequests() {
        return mergeRequests;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Member(){}

    public Member(String username, String name) {

        this.username = username;
        this.name = name;

    }

    public void addProject(Project project) {
        if (!this.projects.contains(project)) {
            this.projects.add(project);
            project.addMember(this);
        }
    }

    public void removeProject(Project project) {
        if (this.projects.contains(project)) {
            this.projects.remove(project);
            project.removeMember(this);
        }
    }

    public void addCommitMapping(CommitMapping commitMapping) {
        if (!this.commitMappings.contains(commitMapping)) {
            this.commitMappings.add(commitMapping);
            commitMapping.setMember(this);
        }
    }

    public void removeCommitMapping(CommitMapping commitMapping) {
        if (this.commitMappings.contains(commitMapping)) {
            this.commitMappings.remove(commitMapping);
            commitMapping.setMember(null);
        }
    }

    public void addMergeRequest(MergeRequest mergeRequest) {
        if (!this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.add(mergeRequest);
            mergeRequest.setMember(this);
        }
    }

    public void removeMergeRequest(MergeRequest mergeRequest) {
        if (this.mergeRequests.contains(mergeRequest)) {
            this.mergeRequests.remove(mergeRequest);
            mergeRequest.setMember(null);
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
