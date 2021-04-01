import React, { useState } from 'react';
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer
} from "recharts";
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import {ScoreDigest} from "../../interfaces/ScoreDigest";
import {formatDay, getMember, setBackground} from "./GraphHelpers"

interface Props {
    data: ScoreDigest[]
    startDateTime: string | undefined,
    endDateTime: string | undefined,
    gitManagementUserId: string | undefined,
}

const Chart = ({data, startDateTime, endDateTime, gitManagementUserId}: Props) => {
    const [commitCheckboxChecked, setCommitCheckboxChecked] = useState(true);
    const [mergeCheckboxChecked, setMergeCheckboxChecked] = useState(true);

    const commitHandleChange = () => {
        setCommitCheckboxChecked(!commitCheckboxChecked);
    }

    const mergeHandleChange = () => {
        setMergeCheckboxChecked(!mergeCheckboxChecked);
    }

    return (
        <div style={{display: "flex", flexDirection: "column"}}>
            <p style={{textAlign: "center"}}>Daily Total Score for Commits and Merge Requests Made By {getMember(gitManagementUserId)}</p>
            <div style={{display: "flex"}}>
                <ResponsiveContainer width="100%" height={400} minWidth="0">
                <BarChart
                    width={1000}
                    height={350}
                    data={data}
                    margin={{ top: 8, right: 15, left: 15, bottom: 8 }}
                    barCategoryGap={0}
                    barGap={0}
                >
                   <CartesianGrid strokeDasharray="3 3" />
                   <XAxis dataKey="day" tickFormatter={formatDay} label={{ value: "Date", position: "middle", dy: 10}} />
                   <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                   <Tooltip />
                   <Bar key="commit-score" dataKey="commitScore" fill="#82ca9d" background={(props) => setBackground(props, startDateTime, endDateTime)} hide={!commitCheckboxChecked}/>
                   <Bar key="mr-score" dataKey="mergeRequestScore" fill="#8884d8" background={(props) => setBackground(props, startDateTime, endDateTime)} hide={!mergeCheckboxChecked}/>
                </BarChart>
                </ResponsiveContainer>
                <FormGroup>
                    <FormControlLabel
                       control={<Checkbox checked={commitCheckboxChecked} onChange={commitHandleChange} style ={{color: "#82ca9d",}} name="checkedCommitForGraphA"/>}
                       label="Commits"
                    />
                    <FormControlLabel
                       control={<Checkbox checked={mergeCheckboxChecked} onChange={mergeHandleChange} style ={{color: "#8884d8",}} name="checkedMergeRequestForGraphA"/>}
                       label="Merge Requests"
                    />
                </FormGroup>
            </div>
        </div>
    );
}

export default Chart;