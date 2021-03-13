import React, { PureComponent, useEffect, useState } from 'react';
import axios, {AxiosResponse} from "axios";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend
} from "recharts";
import {useRouter} from "next/router";
import {AuthContext} from "../../components/AuthContext";
import AuthView from "../../components/AuthView";
import {useSnackbar} from "notistack";

const data = [
    { date: "Jan 10", commits: 0, mergeRequests: 0},
    { date: "Feb 12", commits: 0, mergeRequests: 0},
    { date: "Feb 13", commits: 0, mergeRequests: 0},
    { date: "Feb 14", commits: 0, mergeRequests: 0},
    { date: "Feb 15", commits: 0, mergeRequests: 0},
    { date: "Feb 16", commits: 0, mergeRequests: 0},
    { date: "Feb 17", commits: 0, mergeRequests: 0},
    { date: "Feb 18", commits: 0, mergeRequests: 0},
    { date: "Feb 19", commits: 0, mergeRequests: 0},
    { date: "Feb 20", commits: 0, mergeRequests: 0},
    { date: "Feb 21", commits: 0, mergeRequests: 0},
    { date: "Feb 22", commits: 0, mergeRequests: 0},
    { date: "Feb 23", commits: 0, mergeRequests: 0},
    { date: "Feb 24", commits: 0, mergeRequests: 0},
    { date: "Feb 25", commits: 0, mergeRequests: 0},
];

class Chart extends React.Component {
    render() {
        return (
            <BarChart
                width={1000}
                height={350}
                data={data}
                margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
            >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                <YAxis label={{ value: 'Total', angle: -90, position: 'insideLeft' }} />
                <Tooltip />
                <Bar dataKey="commits" fill="#82ca9d" barSize={15}/>
                <Bar dataKey="mergeRequests" fill="#8884d8" barSize={15}/>
            </BarChart>
        );
    }
}

export default Chart;