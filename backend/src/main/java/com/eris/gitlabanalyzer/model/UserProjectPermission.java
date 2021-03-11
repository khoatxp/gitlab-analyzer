package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.Objects;


@Entity(name = "UserProjectPermission")
@Table(name = "user_project_permission")
public class UserProjectPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_id")
    private Server server;

    public UserProjectPermission() {
    }

    public UserProjectPermission(User user, Project project, Server server) {
        this.user = user;
        this.project = project;
        this.server = server;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Project getProject() {
        return project;
    }

    public Server getServer() {
        return server;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "UserProjectPermission{" +
                "id=" + id +
                ", user=" + user +
                ", project=" + project +
                ", server=" + server +
                '}';
    }
}
