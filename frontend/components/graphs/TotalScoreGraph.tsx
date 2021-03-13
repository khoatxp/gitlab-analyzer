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
import AxisLabel from "./AxisLabel";
import {useRouter} from "next/router";
import {AuthContext} from "../../components/AuthContext";
import AuthView from "../../components/AuthView";
import {useSnackbar} from "notistack";

const data = [
    { date: "Jan 10", commitScore: 40, mergeRequestScore: 4},
    { date: "Feb 12", commitScore: 30, mergeRequestScore: 40},
    { date: "Feb 13", commitScore: 20, mergeRequestScore: 130},
    { date: "Feb 14", commitScore: 27, mergeRequestScore: 100},
    { date: "Feb 15", commitScore: 18, mergeRequestScore: 40},
    { date: "Feb 16", commitScore: 23, mergeRequestScore: 56},
    { date: "Feb 17", commitScore: 12, mergeRequestScore: 23},
    { date: "Feb 18", commitScore: 34, mergeRequestScore: 44},
    { date: "Feb 19", commitScore: 34, mergeRequestScore: 12},
    { date: "Feb 20", commitScore: 3, mergeRequestScore: 34},
    { date: "Feb 21", commitScore: 34, mergeRequestScore: 72},
    { date: "Feb 22", commitScore: 37, mergeRequestScore: 26},
    { date: "Feb 23", commitScore: 23, mergeRequestScore: 38},
    { date: "Feb 24", commitScore: 349, mergeRequestScore: 2},
    { date: "Feb 25", commitScore: 34, mergeRequestScore: 9},
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
                <YAxis label={<AxisLabel axisType="yAxis">Total Score</AxisLabel>} />
                <Tooltip />
                <Bar dataKey="commitScore" fill="#82ca9d" barSize={15}/>
                <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15}/>
            </BarChart>
        );
    }
}

export default Chart;