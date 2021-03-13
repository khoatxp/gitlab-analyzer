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
    { date: "Jan 10", commits: 100},
    { date: "Feb 12", commits: 140},
    { date: "Feb 13", commits: 200},
    { date: "Feb 14", commits: 35},
    { date: "Feb 15", commits: 15},
    { date: "Feb 16", commits: 23},
    { date: "Feb 17", commits: 34},
    { date: "Feb 18", commits: 44},
    { date: "Feb 19", commits: 34},
    { date: "Feb 20", commits: 24},
    { date: "Feb 21", commits: 34},
    { date: "Feb 22", commits: 34},
    { date: "Feb 23", commits: 230},
    { date: "Feb 24", commits: 349},
    { date: "Feb 25", commits: 100},
];

class Chart extends React.Component {
    render() {
        return (
            <BarChart
                width={1000}
                height={350}
                data={data}
                margin={{ top: 8, right: 30, left: 20, bottom: 10 }}
            >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
                <YAxis label={<AxisLabel axisType="yAxis">Total Count</AxisLabel>} />
                <Tooltip />
                <Bar dataKey="commits" fill="#82ca9d" barSize={15} />
            </BarChart>
        );
    }
}

export default Chart;