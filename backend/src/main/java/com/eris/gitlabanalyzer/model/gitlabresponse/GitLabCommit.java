package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@lombok.Getter
public class GitLabCommit {
    @JsonProperty("id")
    private String sha;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    private String title;

    @JsonProperty("web_url")
    private String webUrl;

    public GitLabCommit() { }
}
