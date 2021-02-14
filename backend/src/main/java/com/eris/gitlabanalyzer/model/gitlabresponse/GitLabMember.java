package com.eris.gitlabanalyzer.model.gitlabresponse;

public class GitLabMember {
    private Long id;
    private String username;
    private String name;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public GitLabMember(){}

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +

                '}';
    }
}
