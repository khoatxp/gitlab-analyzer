import React, { PureComponent } from 'react';
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

const data = [
  { date: "January, 10", commits: 0, mergeRequests: 0, amt: 2400 },
  { date: "February 12", commits: 0, mergeRequests: 0, amt: 2210 },
  { date: "February 13", commits: 0, mergeRequests: 0, amt: 2290 },
  { date: "February 14", commits: 0, mergeRequests: 0, amt: 2000 },
  { date: "February 15", commits: 0, mergeRequests: 0, amt: 2181 },
  { date: "February 16", commits: 0, mergeRequests: 0, amt: 2500 },
  { date: "February 17", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 18", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 19", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 20", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 21", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 22", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 23", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 24", commits: 0, mergeRequests: 0, amt: 2100 },
  { date: "February 25", commits: 0, mergeRequests: 0, amt: 2100 },
];

class Chart extends React.Component {
  render() {
    return (
      <BarChart
        width={1000}
        height={300}
        data={data}
        margin={{ top: 8, right: 30, left: 20, bottom: 8 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="date" label={{ value: "Date", position: "middle", dy: 10}} />
        <YAxis
            label={<AxisLabel axisType="yAxis">Total</AxisLabel>}
        />
        <Tooltip />
        <Bar dataKey="commits" fill="#82ca9d" barSize={15}/>
        <Bar dataKey="mergeRequests" fill="#8884d8" barSize={15}/>
      </BarChart>
    );
  }
}

export default Chart;