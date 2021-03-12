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
    const [commitScore, setCommitScore] = React.useState<number>();

    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
    const { projectId, startDateTime, endDateTime } =  router.query;

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/data/projects/${projectId}/commits/score?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setCommitScore(resp.data);
                }).catch(() => {
                    enqueueSnackbar('Failed to get commits score.', {variant: 'error',});
            });
        }
    }, [projectId]);
}

const data = [
    { date: "Jan 10", commitScore: 40},
    { date: "Feb 12", commitScore: 30},
    { date: "Feb 13", commitScore: 20},
    { date: "Feb 14", commitScore: 27},
    { date: "Feb 15", commitScore: 18},
    { date: "Feb 16", commitScore: 23},
    { date: "Feb 17", commitScore: 32},
    { date: "Feb 18", commitScore: 34},
    { date: "Feb 19", commitScore: 3},
    { date: "Feb 20", commitScore: 3},
    { date: "Feb 21", commitScore: 34},
    { date: "Feb 22", commitScore: 37},
    { date: "Feb 23", commitScore: 34},
    { date: "Feb 24", commitScore: 30},
    { date: "Feb 25", commitScore: 34},
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
            </BarChart>
        );
    }
}

export default Chart;
