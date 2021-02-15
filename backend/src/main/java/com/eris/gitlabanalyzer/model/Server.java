package com.eris.gitlabanalyzer.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Server")
@Table(
        name = "server",
        uniqueConstraints={@UniqueConstraint(columnNames={"server_url", "access_token"})}
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

    @Column(
            name = "access_token",
            nullable = false
    )
    private String accessToken;

    @Column(
            name = "token_owner",
            nullable = false
    )
    private String token_owner;

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

    public Server(String serverUrl, String accessToken, String token_owner) {
        this.serverUrl = serverUrl;
        this.accessToken = accessToken;
        this.token_owner = token_owner;
    }

    public Long getId() {
        return id;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getToken_owner() {
        return token_owner;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.setServer(this);
        }
    }

    public void removeMember(Member member) {
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.setServer(null);
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", serverUrl='" + serverUrl + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", token_owner='" + token_owner + '\'' +
                '}';
    }
}
