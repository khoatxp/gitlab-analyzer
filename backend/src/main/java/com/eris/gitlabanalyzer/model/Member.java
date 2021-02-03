package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
@Entity
@Table(name = "member")
public class Member {
    @Id
    private Long id;
    private String username;
    private String name;
    private int accessLevel;

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

    public int getAccessLevel() {
        return accessLevel;
    }


    public Member(){}
    public Member(Long id, String username, String name, int accessLevel, Project project) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.accessLevel = accessLevel;
        this.project = project;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", accessLevel=" + accessLevel +

                '}';
    }
}
