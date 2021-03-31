import {Author} from "./GitLabAuthor";

export interface Note {
    id: number;
    body: string;
    author: Author;
    created_at: string;
    own: boolean;
    parent_iid: number;
    parent_web_url: string;
}
