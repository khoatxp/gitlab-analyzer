package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

@lombok.Getter
public class GitLabCommit {
    @JsonProperty("id")
    private String sha;

    @JsonProperty("author_name")
    private String authorName;

    @JsonProperty("author_email")
    private String authorEmail;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    private String title;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("parent_ids")
    private List<String> parentShas;

    public GitLabCommit() { }
}
