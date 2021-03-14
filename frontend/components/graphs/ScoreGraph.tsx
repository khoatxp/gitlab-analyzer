import React, { PureComponent, useEffect, useState } from 'react';
import axios, {AxiosResponse} from "axios";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";
import {useRouter} from "next/router";
import {AuthContext} from "../../components/AuthContext";
import AuthView from "../../components/AuthView";
import {useSnackbar} from "notistack";

import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox, { CheckboxProps } from '@material-ui/core/Checkbox';
import { makeStyles, withStyles, Theme, createStyles } from '@material-ui/core/styles';

const GreenCheckbox = withStyles({
    root: {
        color: "#82ca9d",
        '&$checked': {
            color: "#82ca9d",
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

const PurpleCheckbox = withStyles({
    root: {
        color: "#8884d8",
        '&$checked': {
            color: "#8884d8",
        },
    },
    checked: {},
})((props: CheckboxProps) => <Checkbox color="default" {...props} />);

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        graphContainer: {
            display: 'flex',
        },
        graphTitleText: {
            textAlign: 'center',
            fontSize: '1.2em',
        },
    }),
);

// TODO: Get graph data (commit score, mr score on a given date) from endpoints
const data = [
    { date: "Jan 10", commitScore: 10, mergeRequestScore: 4},
    { date: "Feb 12", commitScore: 20, mergeRequestScore: 40},
    { date: "Feb 13", commitScore: 30, mergeRequestScore: 50},
    { date: "Feb 14", commitScore: 17, mergeRequestScore: 50},
    { date: "Feb 15", commitScore: 8, mergeRequestScore: 38},
    { date: "Feb 16", commitScore: 10, mergeRequestScore: 56},
    { date: "Feb 17", commitScore: 10, mergeRequestScore: 23},
    { date: "Feb 18", commitScore: 20, mergeRequestScore: 44},
    { date: "Feb 19", commitScore: 12, mergeRequestScore: 12},
    { date: "Feb 20", commitScore: 3, mergeRequestScore: 28},
    { date: "Feb 21", commitScore: 14, mergeRequestScore: 12},
    { date: "Feb 22", commitScore: 20, mergeRequestScore: 26},
    { date: "Feb 23", commitScore: 2, mergeRequestScore: 38},
    { date: "Feb 24", commitScore: 34, mergeRequestScore: 2},
];

class Chart extends React.Component {
    state = {
        commitCheckboxChecked: true,
        mergeCheckboxChecked: true,
        graph1Title: "Daily Total Score for Commits & Merge Requests Made By Everyone",
        graph2Title: "Daily Total Score for Commits Made By Everyone",
        graph3Title: "Daily Total Score for Merge Requests Made By Everyone",
    }

    commitHandleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
         if(this.state.commitCheckboxChecked === true) {
            this.setState({commitCheckboxChecked: false});
         } else if(this.state.commitCheckboxChecked === false) {
            this.setState({commitCheckboxChecked:true});
         }
    };

    mergeHandleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if(this.state.mergeCheckboxChecked === true) {
            this.setState({mergeCheckboxChecked: false});
        } else if(this.state.mergeCheckboxChecked === false) {
            this.setState({mergeCheckboxChecked:true});
        }
    };

    render() {
        if (this.state.commitCheckboxChecked===true && this.state.mergeCheckboxChecked===true) {
            return (
                <>
                    <ResponsiveContainer width="100%" height={400} minWidth="0">
                    <BarChart
                       width={1000}
                       height={350}
                       data={data}
                       margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                    >
                       <CartesianGrid strokeDasharray="3 3" />
                       <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                       <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                       <Tooltip />
                       <Bar dataKey="commitScore" fill="#82ca9d" barSize={15}/>
                       <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15}/>
                    </BarChart>
                    </ResponsiveContainer>
                    <FormGroup>
                        <FormControlLabel
                           control={<GreenCheckbox checked={this.state.commitCheckboxChecked} onChange={this.commitHandleChange} name="checkedCommitForGraphA"/>}
                           label="Commits"
                        />
                        <FormControlLabel
                           control={<PurpleCheckbox checked={this.state.mergeCheckboxChecked} onChange={this.mergeHandleChange} name="checkedMergeRequestForGraphA"/>}
                           label="Merge Requests"
                        />
                    </FormGroup>
                </>
            );
        }  else if (this.state.commitCheckboxChecked===true && this.state.mergeCheckboxChecked===false){
            return(
                <>
                    <ResponsiveContainer width="100%" height={400} minWidth="0">
                        <BarChart
                            width={1000}
                            height={350}
                            data={data}
                            margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                            <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                            <Tooltip />
                            <Bar dataKey="commitScore" fill="#82ca9d" barSize={15}/>
                        </BarChart>
                    </ResponsiveContainer>
                    <FormGroup>
                        <FormControlLabel
                            control={<GreenCheckbox checked={this.state.commitCheckboxChecked} onChange={this.commitHandleChange} name="checkedCommitForGraphA"/>}
                            label="Commits"
                        />
                        <FormControlLabel
                            control={<PurpleCheckbox checked={this.state.mergeCheckboxChecked} onChange={this.mergeHandleChange} name="checkedMergeRequestForGraphA"/>}
                            label="Merge Requests"
                        />
                    </FormGroup>
                </>
            );
        } else if (this.state.commitCheckboxChecked===false && this.state.mergeCheckboxChecked===true){
            return(
                <>
                    <ResponsiveContainer width="100%" height={400} minWidth="0">
                        <BarChart
                            width={1000}
                            height={350}
                            data={data}
                            margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                            <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                            <Tooltip />
                            <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15} />
                        </BarChart>
                    </ResponsiveContainer>
                    <FormGroup>
                        <FormControlLabel
                            control={<GreenCheckbox checked={this.state.commitCheckboxChecked} onChange={this.commitHandleChange} name="checkedCommitForGraphA"/>}
                            label="Commits"
                        />
                        <FormControlLabel
                            control={<PurpleCheckbox checked={this.state.mergeCheckboxChecked} onChange={this.mergeHandleChange} name="checkedMergeRequestForGraphA"/>}
                            label="Merge Requests"
                        />
                    </FormGroup>
                </>
            );
        } else if (this.state.commitCheckboxChecked===false && this.state.mergeCheckboxChecked===false){
            return(
                <>
                    <ResponsiveContainer width="100%" height={400} minWidth="0">
                        <BarChart
                            width={1000}
                            height={350}
                            data={data}
                            margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                            <YAxis label={{ value: 'Total Score', angle: -90, position: 'insideLeft' }} />
                            <Tooltip />
                        </BarChart>
                    </ResponsiveContainer>
                    <FormGroup>
                        <FormControlLabel
                            control={<GreenCheckbox checked={this.state.commitCheckboxChecked} onChange={this.commitHandleChange} name="checkedCommitForGraphA"/>}
                            label="Commits"
                        />
                        <FormControlLabel
                            control={<PurpleCheckbox checked={this.state.mergeCheckboxChecked} onChange={this.mergeHandleChange} name="checkedMergeRequestForGraphA"/>}
                            label="Merge Requests"
                        />
                    </FormGroup>
                </>
            );
        }
    }
}

export default Chart;