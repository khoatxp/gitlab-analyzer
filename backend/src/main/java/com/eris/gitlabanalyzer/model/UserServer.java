package com.eris.gitlabanalyzer.model;

import com.eris.gitlabanalyzer.utils.AttributeEncryptor;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = "UserServer")
@Table(name = "user_server")

public class UserServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_id")
    private Server server;

    @Column(name="access_token")
    @Convert(converter = AttributeEncryptor.class)
    private String accessToken;

    public UserServer(){}

    public UserServer(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Server getServer() {
        return server;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public UserServer(User user, Server server, String accessToken) {
        this.user = user;
        this.server = server;
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserServer that = (UserServer) o;
        return Objects.equals(user, that.user) && Objects.equals(server, that.server);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, server);
    }
}
