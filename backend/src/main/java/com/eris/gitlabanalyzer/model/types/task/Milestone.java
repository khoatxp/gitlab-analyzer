package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@lombok.Getter
public class Milestone {
    private Long id;
    private Long iid;
    @JsonProperty("project_id")
    private int projectId;
    private String title;
    private String description;
    private String state;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    @JsonProperty("due_date")
    private String dueDate;
    @JsonProperty("start_date")
    private String startDate;
    private boolean expired;
    @JsonProperty("web_url")
    private String webUrl;
}