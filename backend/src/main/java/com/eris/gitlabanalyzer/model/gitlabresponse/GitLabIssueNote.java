package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.eris.gitlabanalyzer.model.types.Author;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@lombok.Getter
public class GitLabIssueNote {
    private Long id;
    private String type;
    private String body;
    private String attachment;
    private Author author;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    private boolean system;
    @JsonProperty("noteable_id")
    private Long noteableId;
    @JsonProperty("noteable_type")
    private String noteableType;
    private boolean resolvable;
    private boolean confidential;
    @JsonProperty("noteable_iid")
    private Long noteableIid;
}