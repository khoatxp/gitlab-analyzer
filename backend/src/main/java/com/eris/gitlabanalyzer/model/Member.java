package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Member")
@Table(name = "member")
public class Member {

    @Id
    @Column(
            name = "user_name",
            nullable = false

    )
    private String username;

    @Column(
            name = "name",
            nullable = false

    )
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "member_project_id_fk"
            )
    )
    private Project project;

    @OneToMany(
            mappedBy = "member",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();


    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Member(){}

    public Member(String username, String name, Project project) {
        this.username = username;
        this.name = name;
        this.project = project;
    }

    @Override
    public String toString() {
        return "Member{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", project=" + project +
                '}';
    }
}
