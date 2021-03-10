import {Task} from "./GitLabTask";
import {Author} from "./GitLabAuthor";

export interface MergeRequest extends Task {
    reviewers: Author[];
    sha: string;
    merge_commit_sha: null | string;
}
