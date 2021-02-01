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
    private String lastName;

    public Repo() {
    }

    public Repo(Long id, String firstName, String lastName) {
        this.id = id;
        this.namespace = firstName;
        this.lastName = lastName;
    }

    public Repo(String firstName, String lastName) {
        this.namespace = firstName;
        this.lastName = lastName;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + namespace + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
