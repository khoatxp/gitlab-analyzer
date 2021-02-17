package com.eris.gitlabanalyzer.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Server")
@Table(
        name = "server"
)
public class Server {
    @Id
    @SequenceGenerator(
            name = "server_sequence",
            sequenceName = "server_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "server_sequence"
    )
    @Column(
            name = "server_id"
    )
    private Long id;

    @Column(
            name = "server_url"
    )
    private String serverUrl;

    @OneToMany(
            mappedBy = "server",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Project> projects = new ArrayList<>();

    @OneToMany(
            mappedBy = "server",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<GitLabUser> gitLabUsers = new ArrayList<>();

    @OneToMany(
            mappedBy = "server",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<UserServer> users = new ArrayList<>();

    public Server() {
    }

    public Server(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Long getId() {
        return id;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public List<UserServer> getUsers() {
        return users;
    }

    public void addProject(Project project) {
        if (!this.projects.contains(project)) {
            this.projects.add(project);
            project.setServer(this);
        }
    }

    public void removeProject(Project project) {
        if (this.projects.contains(project)) {
            this.projects.remove(project);
            project.setServer(null);
        }
    }

    public void addGitLabUser(GitLabUser gitLabUser) {
        if (!this.gitLabUsers.contains(gitLabUser)) {
            this.gitLabUsers.add(gitLabUser);
            gitLabUser.setServer(this);
        }
    }

    public void removeGitLabUser(GitLabUser gitLabUser) {
        if (this.gitLabUsers.contains(gitLabUser)) {
            this.gitLabUsers.remove(gitLabUser);
            gitLabUser.setServer(null);
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", serverUrl='" + serverUrl + '\'' +
                '}';
    }
}
