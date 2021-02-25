import {Task} from "./GitLabTask";

export interface Issue extends Task {
    state: IssueState;
    merge_requests_count: number;
    due_date: null | string;
    confidential: boolean;
    has_tasks: boolean;
    _links: Links;
    moved_to_id: null;
    service_desk_reply_to: null;
}

export interface Links {
    self: string;
    notes: string;
    award_emoji: string;
    project: string;
}

export enum IssueState {
    Closed = "closed",
    Opened = "opened",
}

