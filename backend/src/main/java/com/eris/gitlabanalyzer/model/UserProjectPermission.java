package com.eris.gitlabanalyzer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity(name = "UserProjectPermission")
@Table(name = "user_project_permission",
        uniqueConstraints=@UniqueConstraint(columnNames={"user_id", "project_id", "server_id"}))
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
}
