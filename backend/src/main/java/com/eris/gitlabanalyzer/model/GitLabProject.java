package com.eris.gitlabanalyzer.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabProject {
    private Long id;

    private String name;

    @JsonProperty("name_with_namespace")
    private String nameWithNamespace;

    @JsonProperty("web_url")
    private String webUrl;

    public GitLabProject(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameWithNamespace() {
        return nameWithNamespace;
    }

    public String getWebUrl() {
        return webUrl;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameWithNamespace='" + nameWithNamespace + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
