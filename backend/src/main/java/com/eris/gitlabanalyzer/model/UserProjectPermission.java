package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
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

}
