export interface AnalysisRun {
    id: number;
    projectId: number;
    projectNameWithNamespace: string;
    projectName: string;
    status: AnalysisRunStatus;
    startDateTime: string;
    endDateTime: string;
    createdDateTime: string;
    scoreProfileId: number;
    scoreProfileName: string;
}

export enum AnalysisRunStatus {
    InProgress = "In Progress",
    Completed = "Completed",
    Error = "Error",
}