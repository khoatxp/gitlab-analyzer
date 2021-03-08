import {Author} from "./GitLabAuthor";

export interface Note {
    id: number;
    type: null | string;
    body: string;
    author: Author;
    created_at: string;
    noteable_id: number;
    noteable_iid: number;
}
