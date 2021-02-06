package com.eris.gitlabanalyzer.repo;

import javax.persistence.*;

@Entity
@Table
public class Repo {
    @Id
    @SequenceGenerator(
            name="repo_sequence",
            sequenceName = "repo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "repo_sequence"
    )
    private Long id;
    private String namespace;
    @Transient
    private String name;

    public Repo() {
    }

    public Repo(Long id, String namespace) {
        this.id = id;
        this.namespace = namespace;
    }

    public Repo(String namespace) {
        this.namespace = namespace;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return namespace + " REPO!";
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
