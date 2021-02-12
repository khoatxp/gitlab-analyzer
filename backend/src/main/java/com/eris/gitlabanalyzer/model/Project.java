package com.eris.gitlabanalyzer.model;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Project")
@Table(name = "project")
public class Project {
    @Id
    @Column(
            name = "id"
    )
    private Long id;

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
            name = "server_url",
            nullable = false,
            referencedColumnName = "url",
            foreignKey = @ForeignKey(
                    name = "student_book_fk"
            )
    )
    private Server server;

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Member> members = new ArrayList<>();

    @OneToMany(
            mappedBy = "project",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();

    public Project(){}

    public Project(Long id, String name, String nameWithNamespace, String webUrl, Server server) {
        this.id = id;
        this.name = name;
        this.nameWithNamespace = nameWithNamespace;
        this.webUrl = webUrl;
        this.server = server;
    }

    public Long getId() {
        return id;
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

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
            member.setProject(this);
        }
    }

    public void removeMember(Member member) {
        if (this.members.contains(member)) {
            this.members.remove(member);
            member.setProject(null);
        }
    }


}
