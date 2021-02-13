package com.eris.gitlabanalyzer.model;

import java.io.Serializable;
import java.util.Objects;

public class UniqueId implements Serializable {
    private Long id;
    private String serverUrl;
    public UniqueId(){}

    public UniqueId(Long id, String serverUrl) {
        this.id = id;
        this.serverUrl = serverUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniqueId uniqueId = (UniqueId) o;
        return Objects.equals(id, uniqueId.id) && Objects.equals(serverUrl, uniqueId.serverUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverUrl);
    }
}
