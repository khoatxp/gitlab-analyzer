package com.eris.gitlabanalyzer.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Server")
@Table(name = "server")
public class Server {
    @Id
    @Column(
            name = "url"
    )
    private String url;

    @Column(
            name = "access_token",
            nullable = false
    )
    private String accessToken;

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
    private List<Member> members = new ArrayList<>();

    public Server() {
    }

    public Server(String url, String accessToken) {
        this.url = url;
        this.accessToken = accessToken;
    }

    public String getUrl() {
        return url;
    }

    public String getAccessToken() {
        return accessToken;
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

    @Override
    public String toString() {
        return "Server{" +
                "url='" + url + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
