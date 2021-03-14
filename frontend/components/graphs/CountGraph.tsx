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


// TODO: Get graph data (commit count, mr count on a given date) from endpoints
const data = [
    { date: "Jan 10", commits: 0, mergeRequests: 0},
    { date: "Feb 12", commits: 14, mergeRequests: 1},
    { date: "Feb 13", commits: 10, mergeRequests: 1},
    { date: "Feb 14", commits: 0, mergeRequests: 0},
    { date: "Feb 15", commits: 15, mergeRequests: 4},
    { date: "Feb 16", commits: 23, mergeRequests: 3},
    { date: "Feb 17", commits: 34, mergeRequests: 3},
    { date: "Feb 18", commits: 0, mergeRequests: 0},
    { date: "Feb 19", commits: 0, mergeRequests: 0},
    { date: "Feb 20", commits: 0, mergeRequests: 0},
    { date: "Feb 21", commits: 14, mergeRequests: 2},
    { date: "Feb 22", commits: 12, mergeRequests: 1},
    { date: "Feb 23", commits: 25, mergeRequests: 2},
    { date: "Feb 24", commits: 16, mergeRequests: 2},
];

class Chart extends React.Component {
    state = {
        commitCheckboxChecked: true,
        mergeCheckboxChecked: true,
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
        const { classes } = this.props;
        if (this.state.commitCheckboxChecked===true && this.state.mergeCheckboxChecked===true) {
            return (
                <div style={{display: "flex", flexDirection: "column"}}>
                    <p style={{textAlign: "center"}}>Daily Total Commits and Merge Requests Made By Everyone</p>
                    <div style={{display: "flex"}}>
                        <ResponsiveContainer width="100%" height={400} minWidth="0">
                            <BarChart
                               width={1000}
                               height={350}
                               data={data}
                               margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                            >
                               <CartesianGrid strokeDasharray="3 3" />
                               <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                               <YAxis label={{ value: 'Total Count', angle: -90, position: 'insideLeft' }} />
                               <Tooltip />
                               <Bar dataKey="commits" fill="#82ca9d" barSize={15}/>
                               <Bar dataKey="mergeRequests" fill="#8884d8" barSize={15}/>
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
                    </div>
                </div>
            );
        }  else if (this.state.commitCheckboxChecked===true && this.state.mergeCheckboxChecked===false){
            return(
                <div style={{display: "flex", flexDirection: "column"}}>
                    <p style={{textAlign: "center"}}>Daily Total Commits Made By Everyone</p>
                    <div style={{display: "flex"}}>
                        <ResponsiveContainer width="100%" height={400} minWidth="0">
                            <BarChart
                                width={1000}
                                height={350}
                                data={data}
                                margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                                <YAxis label={{ value: 'Total Count', angle: -90, position: 'insideLeft' }} />
                                <Tooltip />
                                <Bar dataKey="commits" fill="#82ca9d" barSize={15} />
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
                    </div>
                </div>
            );
        } else if (this.state.commitCheckboxChecked===false && this.state.mergeCheckboxChecked===true){
            return(
                <div style={{display: "flex", flexDirection: "column"}}>
                    <p style={{textAlign: "center"}}>Daily Total Merge Requests Made By Everyone</p>
                    <div style={{display: "flex"}}>
                        <ResponsiveContainer width="100%" height={400} minWidth="0">
                            <BarChart
                                width={1000}
                                height={350}
                                data={data}
                                margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                                <YAxis label={{ value: 'Total Count', angle: -90, position: 'insideLeft' }} />
                                <Tooltip />
                                <Bar dataKey="mergeRequests" fill="#8884d8" barSize={15} />
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
                    </div>
                </div>
            );
        } else if (this.state.commitCheckboxChecked===false && this.state.mergeCheckboxChecked===false){
            return(
                <div style={{display: "flex", flexDirection: "column"}}>
                    <p style={{textAlign: "center"}}>Daily Total Commits and Merge Requests Made By Everyone</p>
                    <div style={{display: "flex"}}>
                        <ResponsiveContainer width="100%" height={400} minWidth="0">
                            <BarChart
                                width={1000}
                                height={350}
                                data={data}
                                margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
                            >
                                <CartesianGrid strokeDasharray="3 3" />
                                <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                                <YAxis label={{ value: 'Total Count', angle: -90, position: 'insideLeft' }} />
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
                    </div>
                </div>
            );
        }
    }
}

export default Chart;