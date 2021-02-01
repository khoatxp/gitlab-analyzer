package com.eris.gitlabanalyzer.repo;

import javax.persistence.*;

@Entity
@Table
public class Repo {
    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String namespace;
    @Transient
    private String name;

    public Repo() {
    }

    public Repo(Long id, String nameSpace) {
        this.id = id;
        this.namespace = nameSpace;
    }

    public Repo(String nameSpace) {
        this.namespace = nameSpace;
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

    public void setNamespace(String firstName) {
        this.namespace = firstName;
    }

    public String getName() {
        return namespace + " REPO!";
    }

    public void setName(String lastName) {
        this.name = lastName;
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
