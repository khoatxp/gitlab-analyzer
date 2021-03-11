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
  { name: "January, 10, 2020", commits: 4000, amt: 2400 },
  { name: "February 12, 2020", commits: 3000, amt: 2210 },
  { name: "February 13, 2020", commits: 2000, amt: 2290 },
  { name: "February 14, 2020", commits: 2780, amt: 2000 },
  { name: "February 15, 2020", commits: 1890, amt: 2181 },
  { name: "February 16, 2020", commits: 2390, amt: 2500 },
  { name: "February 17, 2020", commits: 3490, amt: 2100 },
  { name: "February 17, 2020", commits: 3490, amt: 2100 },
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
        <CartesianGrid strokeDasharray="1 1" />
        <XAxis label={Date} />
        <YAxis
            label={<AxisLabel axisType="yAxis">Total Count</AxisLabel>}
        />
        <Tooltip />
        <Bar dataKey="commits" fill="#82ca9d" barSize={15}/>
      </BarChart>
    );
  }
}

export default Chart;
