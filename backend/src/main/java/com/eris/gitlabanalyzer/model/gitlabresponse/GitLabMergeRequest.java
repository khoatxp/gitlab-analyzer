package com.eris.gitlabanalyzer.model.gitlabresponse;

import com.eris.gitlabanalyzer.model.types.Author;
import com.eris.gitlabanalyzer.model.types.task.Milestone;
import com.eris.gitlabanalyzer.model.types.task.References;
import com.eris.gitlabanalyzer.model.types.task.TaskCompletionStatus;
import com.eris.gitlabanalyzer.model.types.task.TimeStats;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.ToString;

@ToString
@lombok.Getter
public class GitLabMergeRequest {
    private Long id;
    private Long iid;
    @JsonProperty("project_id")
    private Long projectId;
    private String title;
    private String description;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("merged_by")
    private Author mergedBy;
    @JsonProperty("merged_at")
    private OffsetDateTime mergedAt;
    private Author author;
    private String sha;
    @JsonProperty("merge_commit_sha")
    private String mergeCommitSha;
    @JsonProperty("web_url")
    private String webUrl;
}
