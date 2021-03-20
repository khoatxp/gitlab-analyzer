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
            name = "server_url",
            unique = true
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
    private List<GitManagementUser> gitManagementUsers = new ArrayList<>();

    @OneToMany(
            mappedBy = "server",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<UserServer> userServers = new ArrayList<>();

    @OneToMany(
            mappedBy = "server",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<UserProjectPermission> userProjectPermissions = new ArrayList<>();

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

    public List<UserServer> getUserServers() {
        return userServers;
    }

    public void addProject(Project project) {
        if (!this.projects.contains(project)) {
            this.projects.add(project);
            project.setServer(this);
        }
    }

    public void addGitManagementUser(GitManagementUser gitManagementUser) {
        if (!this.gitManagementUsers.contains(gitManagementUser)) {
            this.gitManagementUsers.add(gitManagementUser);
            gitManagementUser.setServer(this);
        }
    }

    public void addUserServer(UserServer userServer) {
        if (!this.userServers.contains(userServer)) {
            this.userServers.add(userServer);
            userServer.setServer(this);
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
