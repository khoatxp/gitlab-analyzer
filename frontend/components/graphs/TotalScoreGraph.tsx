import React from "react";
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
  { name: "January, 10, 2020", commitScore: 4000, mergeRequestScore: 2400, amt: 2400 },
  { name: "February 12, 2020", commitScore: 3000, mergeRequestScore: 1398, amt: 2210 },
  { name: "February 13, 2020", commitScore: 2000, mergeRequestScore: 9800, amt: 2290 },
  { name: "February 14, 2020", commitScore: 2780, mergeRequestScore: 3908, amt: 2000 },
  { name: "February 15, 2020", commitScore: 1890, mergeRequestScore: 4800, amt: 2181 },
  { name: "February 16, 2020", commitScore: 2390, mergeRequestScore: 3800, amt: 2500 },
  { name: "February 17, 2020", commitScore: 3490, mergeRequestScore: 4300, amt: 2100 }
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
        <XAxis dataKey="name" />
        <YAxis
          label={<AxisLabel axisType="yAxis">Total Score</AxisLabel>}
        />
        <Tooltip />
        <Bar dataKey="commitScore" fill="#82ca9d" barSize={15}/>
        <Bar dataKey="mergeRequestScore" fill="#8884d8" barSize={15}/>
      </BarChart>
    );
  }
}

export default Chart;