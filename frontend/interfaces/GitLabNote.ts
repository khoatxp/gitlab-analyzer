export interface Note {
    id: number;
    type: null | string;
    body: string;
    attachment: null;
    author: Author;
    created_at: string;
    updated_at: string;
    system: boolean;
    noteable_id: number;
    noteable_type: string;
    resolvable: boolean;
    confidential: boolean;
    noteable_iid: number;
}

export interface MergeRequestNote extends Note {
    commit_id?: null;
    position?: Position;
    resolved?: boolean;
    resolved_by?: null;
}

export interface IssueNote extends Note {
}

export interface Author {
    id: number;
    name: string;
    username: string;
    state: string;
    avatar_url: null | string;
    web_url: string;
}

export interface Position {
    base_sha: string;
    start_sha: string;
    head_sha: string;
    old_path: string;
    new_path: string;
    position_type: string;
    old_line: number;
    new_line: number;
    line_range: LineRange;
}

export interface LineRange {
    start: Line;
    end: Line;
}

export interface Line {
    line_code: string;
    type: string;
    old_line: number;
    new_line: number;
}
