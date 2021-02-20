package com.eris.gitlabanalyzer.model;

import com.eris.gitlabanalyzer.model.types.Author;
import com.eris.gitlabanalyzer.model.types.task.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

@lombok.Data
public class GitLabIssue {
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
    @JsonProperty("closed_at")
    private OffsetDateTime closedAt;
    @JsonProperty("closed_by")
    private Author closedBy;
    private List<String> labels;
    private Milestone milestone;
    private List<Author> assignees;
    private Author author;
    private Author assignee;
    @JsonProperty("user_notes_count")
    private int userNotesCount;
    @JsonProperty("merge_requests_count")
    private int mergeRequestsCount;
    private int upvotes;
    private int downvotes;
    @JsonProperty("due_date")
    private String dueDate;
    private boolean confidential;
    @JsonProperty("discussion_locked")
    private String discussionLocked;
    @JsonProperty("web_url")
    private String webUrl;
    @JsonProperty("time_stats")
    private TimeStats timeStats;
    @JsonProperty("task_completion_status")
    private TaskCompletionStatus taskCompletionStatus;
    @JsonProperty("has_tasks")
    private boolean hasTasks;
    @JsonProperty("_links")
    private Links Links;
    private References references;
    @JsonProperty("moved_to_id")
    private String movedToId;
    @JsonProperty("service_desk_reply_to")
    private String serviceDeskReplyTo;
}