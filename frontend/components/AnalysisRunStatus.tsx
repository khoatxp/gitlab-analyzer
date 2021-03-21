import {Avatar, Box, Typography} from "@material-ui/core";
import React from "react";

type AnalysisRunStatusProps = { status: string }
const AnalysisRunStatus = ({status}: AnalysisRunStatusProps) => {
    return (
        <Box display="flex" alignItems="center">
            <Box m={1}>
                <Avatar style={{width: "1em", height: "1em", backgroundColor: getColourFromStatus(status)}}>{' '}</Avatar>
            </Box>
            <Typography variant="subtitle2">{status || 'Error'}</Typography>
        </Box>
    )
}

const getColourFromStatus = (status: string) => {
    switch (status) {
        case 'Complete':
            return 'green'
        case 'In progress':
            return 'yellow'
        default:
            return 'crimson'
    }
}

export default AnalysisRunStatus;