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
import {parseISO} from "date-fns";

interface Props {
    data: ScoreDigest[]
}

const Chart = ({data}: Props) => {
    const [commitCheckboxChecked, setCommitCheckboxChecked] = useState(true);
    const [mergeCheckboxChecked, setMergeCheckboxChecked] = useState(true);

    const commitHandleChange = () => {
        setCommitCheckboxChecked(!commitCheckboxChecked);
    }

    const mergeHandleChange = () => {
        setMergeCheckboxChecked(!mergeCheckboxChecked);
    }

    const formatDay = (day:string) => {
        return parseISO(day).toLocaleDateString('en-Us', {month: 'short', day: 'numeric'});
    }

    return (
        <div style={{display: "flex", flexDirection: "column"}}>
            <p style={{textAlign: "center"}}>Daily Total Score for Commits and Merge Requests Made By Everyone</p>
            <div style={{display: "flex"}}>
                <ResponsiveContainer width="100%" height={400} minWidth="0">
                <BarChart
                   width={1000}
                   height={350}
                   data={data}
                   margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                >
                   <CartesianGrid strokeDasharray="3 3" />
                   <XAxis dataKey="day" tickFormatter={formatDay} label={{ value: "Date", position: "middle", dy: 10}} />
                   <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                   <Tooltip />
                   <Bar dataKey="commitScore" fill="#82ca9d" barSize={15} hide={!commitCheckboxChecked}/>
                   <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15} hide={!mergeCheckboxChecked}/>
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