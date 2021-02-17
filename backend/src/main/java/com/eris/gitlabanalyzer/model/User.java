package com.eris.gitlabanalyzer.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "User")
@Table(name = "app_user")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "user_id"
    )
    private Long id;

    private String username;

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<UserServer> userServers = new ArrayList<>();


    public User(){}

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {return username;}

    public List<UserServer> getServers() {
        return userServers;
    }

    // I think this code is highly coupled with Server model instead of UserServer
    // We might want to handle this in bigger scope.
    public void addServer(Server server, String accessToken){
        UserServer userServer = new UserServer(this, server, accessToken);
        this.addUserServer(userServer);
        server.addUserServer(userServer);
    }

    public void addUserServer(UserServer userServer) {
        if (!this.userServers.contains(userServer)) {
            this.userServers.add(userServer);
            userServer.setUser(this);
        }
    }

    public void removeUserServer(UserServer userServer) {
        if (this.userServers.contains(userServer)) {
            this.userServers.remove(userServer);
            userServer.setUser(null);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
