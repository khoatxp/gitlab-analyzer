import {Avatar, Box, Paper, Typography} from "@material-ui/core";
import formatDate from "../../utils/DateFormatter";
import AnalysisRunStatusIndicator from "./AnalysisRunStatusIndicator";
import AppButton from "../app/AppButton";
import {AnalysisRun, AnalysisRunStatus} from "../../interfaces/AnalysisRun";
import React from "react";
import {useRouter} from "next/router";

type AnalysisRunListProps = { isLoading: boolean, analysisRuns: AnalysisRun[], loadAnalysisRuns: () => void }
const AnalysisRunList = ({isLoading, analysisRuns, loadAnalysisRuns}: AnalysisRunListProps) => {
    const router = useRouter();

    return (
        <Box>
            <Box maxHeight="50vh" overflow="auto">
                {
                    analysisRuns.map((analysis: any) =>
                        <Paper elevation={4} style={{margin: "1em"}} key={analysis.id}>
                            <Box display="flex" alignItems="center" padding={2.5}>
                                <Avatar variant='rounded' style={{width: '4em', height: '4em'}}>
                                    <Typography variant="h3">
                                        {analysis.projectName[0].toUpperCase()}
                                    </Typography>
                                </Avatar>

                                <Box ml={3} flexGrow={1}>
                                    <Typography variant="h4">{analysis.projectNameWithNamespace}</Typography>
                                    <Typography variant="subtitle2">
                                        <b>From:</b> {formatDate(analysis.startDateTime)} - {formatDate(analysis.endDateTime)}
                                    </Typography>
                                    <Typography variant="subtitle2">
                                        <b>Created:</b> {formatDate(analysis.createdDateTime)}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center">
                                    <AnalysisRunStatusIndicator status={analysis.status}/>
                                    <AppButton
                                        color="primary"
                                        onClick={() => router.push(`/project/${analysis.projectId}/0/overview?startDateTime=${analysis.startDateTime}&endDateTime=${analysis.endDateTime}`)}
                                        disabled={analysis.status != AnalysisRunStatus.Completed}
                                    >
                                        View
                                    </AppButton>
                                </Box>
                            </Box>
                        </Paper>
                    )
                }
            </Box>
            <Box>
                {analysisRuns.length === 0 && !isLoading && <Typography variant="h3" align="center">No analyses found.</Typography>}
                {analysisRuns.length === 0 && isLoading && <Typography variant="h3" align="center">Loading...</Typography>}
            </Box>
            <Box display="flex" justifyContent="center">
                <AppButton
                    color="primary"
                    onClick={loadAnalysisRuns}
                    disabled={isLoading}
                >
                    Sync
                </AppButton>
            </Box>
        </Box>
    )
}

export default AnalysisRunList;