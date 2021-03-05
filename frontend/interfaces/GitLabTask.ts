/* Task is the parent interface of GitLabIssue and GitLabMergeRequest */
import {Author} from "./GitLabAuthor";

export interface Task {
    id: number;
    iid: number;
    project_id: number;
    title: string;
    description: string;
    created_at: string;
    labels: string[];
    assignees: Author[];
    author: Author;
    assignee: Author | null;
    web_url: string;
}
