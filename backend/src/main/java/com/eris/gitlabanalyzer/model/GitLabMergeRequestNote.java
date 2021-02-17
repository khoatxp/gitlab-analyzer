package com.eris.gitlabanalyzer.model;

import com.eris.gitlabanalyzer.model.types.Author;
import com.eris.gitlabanalyzer.model.types.Position;
import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;

@lombok.Data
public class GitLabMergeRequestNote {
    private long id;
    private String type;
    private String body;
    private Object attachment;
    private Author author;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    private boolean system;
    @JsonProperty("noteable_id")
    private long noteableID;
    @JsonProperty("noteable_type")
    private String noteableType;
    @JsonProperty("commit_id")
    private Object commitID;
    private Position position;
    private boolean resolvable;
    private Boolean resolved;
    @JsonProperty("resolved_by")
    private Object resolvedBy;
    private boolean confidential;
    @JsonProperty("noteable_iid")
    private long noteableIid;
}
