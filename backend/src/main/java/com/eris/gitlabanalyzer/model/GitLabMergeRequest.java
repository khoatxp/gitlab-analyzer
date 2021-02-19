package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

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
}
