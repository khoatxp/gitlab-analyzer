package com.eris.gitlabanalyzer.model.gitlabresponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GitLabMergeRequest {
    private Long id;
    private Long iid;
    private String title;
    private String description;
    private String name;
    private String username;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("author")
    @SuppressWarnings("unchecked")
    private void authorDeserializer(Map<String, Object> author) {
        this.name = (String) author.get("name");
        this.username = (String) author.get("username");
    }

    public GitLabMergeRequest(){}

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequest{" +
                "id=" + id +
                ", iid=" + iid +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
