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
 { date: "January, 10", commits: 4000, amt: 2400 },
   { date: "February 12", commits: 3000, amt: 2210 },
   { date: "February 13", commits: 2000, amt: 2290 },
   { date: "February 14", commits: 2780, amt: 2000 },
   { date: "February 15", commits: 1890, amt: 2181 },
   { date: "February 16", commits: 2390, amt: 2500 },
   { date: "February 17", commits: 3490, amt: 2100 },
   { date: "February 18", commits: 3490, amt: 2100 },
   { date: "February 19", commits: 3490, amt: 2100 },
   { date: "February 20", commits: 3490, amt: 2100 },
   { date: "February 21", commits: 3490, amt: 2100 },
   { date: "February 22", commits: 3490, amt: 2100 },
   { date: "February 23", commits: 3490, amt: 2100 },
   { date: "February 24", commits: 3490, amt: 2100 },
   { date: "February 25", commits: 3490, amt: 2100 },
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
        <XAxis dataKey="date" />
        <YAxis
            label={<AxisLabel axisType="yAxis">Total Count</AxisLabel>}
        />
        <Tooltip />
        <Bar dataKey="commits" fill="#8884d8" barSize={15} />
      </BarChart>
    );
  }
}

export default Chart;
