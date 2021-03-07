package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.eris.gitlabanalyzer.model.types.Author;
import com.fasterxml.jackson.annotation.*;
import java.time.OffsetDateTime;

@lombok.Getter
public class GitLabMergeRequestNote {
    private Long id;
    private String type;
    private String body;
    private Author author;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("noteable_id")
    private Long noteableID;
    @JsonProperty("noteable_iid")
    private Long noteableIid;
}
