package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Map;

public class GitLabMergeRequest {
    private Long iid;

    @JsonProperty("project_id")
    private Long projectId;

    private String title;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("merged_at")
    private ZonedDateTime mergedAt;

    @JsonProperty("web_url")
    private String webUrl;

    private String username;
    @JsonProperty("author")
    @SuppressWarnings("unchecked")
    private void authorDeserializer(Map<String, Object> author) {
        this.username = (String) author.get("username");
    }


    public GitLabMergeRequest(){}

    public Long getIid() {
        return iid;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getMergedAt() {
        return mergedAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getUsername() {
        return username;
    }
}
