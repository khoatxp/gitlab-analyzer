import {Author} from "./GitLabAuthor";

export interface Note {
    id: number;
    body: string;
    author: Author;
    createdAt: string;
    own: boolean;
    parentIid: number;
    parentWebUrl: string;
}
