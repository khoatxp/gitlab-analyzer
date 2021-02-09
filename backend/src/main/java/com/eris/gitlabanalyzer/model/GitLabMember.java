package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabMember {
    private Long id;
    private String username;
    private String name;

    @JsonProperty("access_level")
    private int accessLevel;

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

    public GitLabMember(){}

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
