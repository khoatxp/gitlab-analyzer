import {Avatar, Box, Typography} from "@material-ui/core";
import React from "react";
import {AnalysisRunStatus} from "../../interfaces/AnalysisRun";

type AnalysisRunStatusIndicatorProps = { status: AnalysisRunStatus }
const AnalysisRunStatusIndicator = ({status}: AnalysisRunStatusIndicatorProps) => {
    return (
        <Box display="flex" alignItems="center">
            <Box m={1}>
                <Avatar style={{width: "1em", height: "1em", backgroundColor: getColourFromStatus(status)}}>{' '}</Avatar>
            </Box>
            <Typography variant="subtitle2">{status || 'Error'}</Typography>
        </Box>
    )
}

const getColourFromStatus = (status: AnalysisRunStatus) => {
    switch (status) {
        case AnalysisRunStatus.InProgress:
            return 'khaki';
        case AnalysisRunStatus.Completed:
            return 'green';
        case AnalysisRunStatus.Error:
            return 'crimson';
        default:
            // Default to error
            return 'crimson';
    }
}

export default AnalysisRunStatusIndicator;