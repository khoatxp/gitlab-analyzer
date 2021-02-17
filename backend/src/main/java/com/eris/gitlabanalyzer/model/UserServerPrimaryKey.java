package com.eris.gitlabanalyzer.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserServerPrimaryKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "server_id")
    private Long serverId;

    public UserServerPrimaryKey(){}

    public UserServerPrimaryKey(Long userId, Long serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getServerId() {
        return serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserServerPrimaryKey that = (UserServerPrimaryKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, serverId);
    }
}
