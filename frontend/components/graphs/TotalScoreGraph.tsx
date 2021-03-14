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
    render() {
        return (
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
        );
    }
}

export default Chart;