package com.eris.gitlabanalyzer.model;

import com.eris.gitlabanalyzer.model.types.Author;
import com.eris.gitlabanalyzer.model.types.task.Milestone;
import com.eris.gitlabanalyzer.model.types.task.References;
import com.eris.gitlabanalyzer.model.types.task.TaskCompletionStatus;
import com.eris.gitlabanalyzer.model.types.task.TimeStats;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

@lombok.Data
public class GitLabMergeRequest {
    private Long id;
    private Long iid;
    @JsonProperty("project_id")
    private Long projectId;
    private String title;
    private String description;
    private String state;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
    @JsonProperty("merged_by")
    private Author mergedBy;
    @JsonProperty("merged_at")
    private OffsetDateTime mergedAt;
    @JsonProperty("closed_by")
    private Author closedBy;
    @JsonProperty("closed_at")
    private OffsetDateTime closedAt;
    @JsonProperty("target_branch")
    private String targetBranch;
    @JsonProperty("source_branch")
    private String sourceBranch;
    @JsonProperty("user_notes_count")
    private int userNotesCount;
    private int upvotes;
    private int downvotes;
    private Author author;
    private List<String> assignees;
    private String assignee;
    private List<String> reviewers;
    @JsonProperty("source_project_id")
    private Long sourceProjectId;
    @JsonProperty("target_project_id")
    private Long targetProjectId;
    private List<String> labels;
    @JsonProperty("work_in_progress")
    private boolean workInProgress;
    private Milestone milestone;
    @JsonProperty("merge_when_pipeline_succeeds")
    private boolean mergeWhenPipelineSucceeds;
    @JsonProperty("merge_status")
    private String mergeStatus;
    private String sha;
    @JsonProperty("merge_commit_sha")
    private String mergeCommitSha;
    @JsonProperty("squash_commit_sha")
    private String squashCommitSha;
    @JsonProperty("discussion_locked")
    private String discussionLocked;
    @JsonProperty("should_remove_source_branch")
    private String shouldRemoveSourceBranch;
    @JsonProperty("force_remove_source_branch")
    private boolean forceRemoveSourceBranch;
    private String reference;
    private References references;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("time_stats")
    private TimeStats timeStats;
    private boolean squash;
    @JsonProperty("task_completion_status")
    private TaskCompletionStatus taskCompletionStatus;
    @JsonProperty("has_conflicts")
    private boolean hasConflicts;
    @JsonProperty("blocking_discussions_resolved")
    private boolean blockingDiscussionsResolved;
}
