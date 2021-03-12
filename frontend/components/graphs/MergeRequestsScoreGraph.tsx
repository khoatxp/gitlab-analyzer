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

const ChartData = () => {
    const {enqueueSnackbar} = useSnackbar();
    const [date, setDate] = React.useState<[]>();
    const [mergeRequestScore, setMergeRequestScore] = React.useState<number>();

    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const { projectId, startDateTime, endDateTime } =  router.query;

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/merge_requests/score?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setMergeRequestScore(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to get merge request score.', {variant: 'error',});
            });
        }
    }, [projectId]);
}

const data = [
    { date: "Jan 10", mergeRequestScore: 4},
    { date: "Feb 12", mergeRequestScore: 40},
    { date: "Feb 13", mergeRequestScore: 130},
    { date: "Feb 14", mergeRequestScore: 100},
    { date: "Feb 15", mergeRequestScore: 40},
    { date: "Feb 16", mergeRequestScore: 56},
    { date: "Feb 17", mergeRequestScore: 23},
    { date: "Feb 18", mergeRequestScore: 44},
    { date: "Feb 19", mergeRequestScore: 12},
    { date: "Feb 20", mergeRequestScore: 34},
    { date: "Feb 21", mergeRequestScore: 72},
    { date: "Feb 22", mergeRequestScore: 26},
    { date: "Feb 23", mergeRequestScore: 38},
    { date: "Feb 24", mergeRequestScore: 2},
    { date: "Feb 25", mergeRequestScore: 9},
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
                <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15} />
            </BarChart>
        );
    }
}

export default Chart;