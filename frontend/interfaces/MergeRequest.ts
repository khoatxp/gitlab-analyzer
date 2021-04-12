export interface MergeRequest {
    id: number;
    iid: number;
    authorUsername: string;
    authorName: string;
    title: string;
    createdAt: string;
    mergedAt: string;
    webUrl: string;
    ignored: boolean;
}

export interface MergeReturnObject {
    mergeScore: number;
    sharedMergeScore: number;
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
} as unknown as MergeRequest & { secondaryText: string };