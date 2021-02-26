/* Parent interface of GitLabIssue and GitLabMergeRequest */
export interface Task {
    id: number;
    iid: number;
    project_id: number;
    title: string;
    description: string;
    created_at: string;
    updated_at: string;
    closed_at: null | string;
    closed_by: Author | null;
    labels: string[];
    milestone: Milestone | null;
    assignees: Author[];
    author: Author;
    assignee: Author | null;
    user_notes_count: number;
    upvotes: number;
    downvotes: number;
    discussion_locked: null;
    web_url: string;
    time_stats: TimeStats;
    task_completion_status: TaskCompletionStatus;
    references: References;
}

export interface Author {
    id: number;
    name: string;
    username: string;
    state: string;
    avatar_url: null | string;
    web_url: string;
}

export interface References {
    short: string;
    relative: string;
    full: string;
}

export interface Milestone {
    id: number;
    iid: number;
    project_id: number;
    title: string;
    description: string;
    state: string;
    created_at: string;
    updated_at: string;
    due_date: string;
    start_date: null;
    expired: boolean;
    web_url: string;
}

export interface TaskCompletionStatus {
    count: number;
    completed_count: number;
}

export interface TimeStats {
    time_estimate: number;
    total_time_spent: number;
    human_time_estimate: null | string;
    human_total_time_spent: null;
}
