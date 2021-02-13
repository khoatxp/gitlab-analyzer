package com.eris.gitlabanalyzer.model;


import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ProjectId implements Serializable {
    private Long id;
    private String serverUrl;
    public ProjectId(){}

    public ProjectId(Long id, String serverUrl) {
        this.id = id;
        this.serverUrl = serverUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectId projectId = (ProjectId) o;
        return Objects.equals(id, projectId.id) && Objects.equals(serverUrl, projectId.serverUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverUrl);
    }
}
