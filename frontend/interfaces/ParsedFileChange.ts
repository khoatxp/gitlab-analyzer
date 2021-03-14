export type ParsedFileChange = {
    oldPath: string,
    newPath: string,
    oldRevision: string,
    newRevision: string,
    type: string,
    hunks: any[]
}