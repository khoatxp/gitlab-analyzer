export interface FileChange {
    diff: string;
    new_path: string;
    old_path: string;
    renamed_File: boolean;
}
