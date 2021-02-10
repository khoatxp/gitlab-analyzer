package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table
public class Member {
    @Id
    private Long id;
    private String username;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }


    public Member(){}
    public Member(Long id, String username, String name, Project project) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.project = project;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +

                '}';
    }
}
