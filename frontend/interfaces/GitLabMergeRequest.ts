import {Task} from "./GitLabTask";

export interface MergeRequest extends Task {
    state: MergeRequestState;
    merged_by: Author | null;
    merged_at: null | string;
    target_branch: string;
    source_branch: string;
    reviewers: Author[];
    source_project_id: number;
    target_project_id: number;
    work_in_progress: boolean;
    merge_when_pipeline_succeeds: boolean;
    merge_status: MergeStatus;
    sha: string;
    merge_commit_sha: null | string;
    squash_commit_sha: null;
    should_remove_source_branch: null;
    force_remove_source_branch: boolean;
    reference: string;
    squash: boolean;
    has_conflicts: boolean;
    blocking_discussions_resolved: boolean;
}

export interface Author {
    id: number;
    name: string;
    username: string;
    state: string;
    avatar_url: null | string;
    web_url: string;
}

export enum MergeStatus {
    CanBeMerged = "can_be_merged",
    CannotBeMerged = "cannot_be_merged",
    Unchecked = "unchecked",
}

export enum MergeRequestState {
    Closed = "closed",
    Merged = "merged",
    Opened = "opened",
}
