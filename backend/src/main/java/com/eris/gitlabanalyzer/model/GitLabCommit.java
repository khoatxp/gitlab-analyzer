package com.eris.gitlabanalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class GitLabCommit {
    @JsonProperty("id")
    private String sha;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    @JsonProperty("project_id")
    private Long projectId;

    private String title;

    @JsonProperty("web_url")
    private String webUrl;

    public GitLabCommit() { }

    public String getSha() {
        return sha;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
