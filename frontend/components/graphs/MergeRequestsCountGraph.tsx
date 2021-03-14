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
    { date: "Jan 10", mergeRequests: 0},
    { date: "Feb 12", mergeRequests: 1},
    { date: "Feb 13", mergeRequests: 1},
    { date: "Feb 14", mergeRequests: 0},
    { date: "Feb 15", mergeRequests: 4},
    { date: "Feb 16", mergeRequests: 3},
    { date: "Feb 17", mergeRequests: 3},
    { date: "Feb 18", mergeRequests: 0},
    { date: "Feb 19", mergeRequests: 0},
    { date: "Feb 20", mergeRequests: 0},
    { date: "Feb 21", mergeRequests: 2},
    { date: "Feb 22", mergeRequests: 1},
    { date: "Feb 23", mergeRequests: 2},
    { date: "Feb 24", mergeRequests: 2},
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
                    <YAxis label={{ value: 'Total Count', angle: -90, position: 'insideLeft' }} />
                    <Tooltip />
                    <Bar dataKey="mergeRequests" fill="#8884d8" barSize={15} />
                </BarChart>
            </ResponsiveContainer>
        );
    }
}

export default Chart;