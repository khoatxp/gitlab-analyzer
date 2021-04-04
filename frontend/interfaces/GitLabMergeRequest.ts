import {Task} from "./GitLabTask";
import {Author} from "./GitLabAuthor";

export interface MergeRequest extends Task {
    reviewers: Author[];
    sha: string;
    merge_commit_sha: null | string;
}


/*
 * The merge request list item in the code page that
 * displays orphan commits on click.
 *
 * Treated as a "Merge Request" so that it can be easily supplied to the list
 * and rendered
 */
export const OrphanCommitMergeRequest = {
    id: 0,
    createdAt: '',
    author: {name: ''},
    title: 'Orphan Commits',
    secondaryText: 'Commits pushed directly to the main branch'
} as unknown as MergeRequest & { secondaryText: string }