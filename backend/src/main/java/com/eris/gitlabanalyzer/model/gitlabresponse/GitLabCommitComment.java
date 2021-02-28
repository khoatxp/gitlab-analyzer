package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.eris.gitlabanalyzer.model.types.Author;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@lombok.Getter
public class GitLabCommitComment {
    private String note;
    private Author author;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
}
