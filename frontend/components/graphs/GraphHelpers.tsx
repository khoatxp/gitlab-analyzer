import React from "react";
import {parseISO} from "date-fns";
import {Bar} from "recharts";


const formatDay = (day:string) => {
    return parseISO(day).toLocaleDateString('en-Us', {month: 'short', day: 'numeric'});
}

const setBackground = (props:any, startDateTime:string, endDateTime:string) => {
    if (parseISO(props.day) >= parseISO(startDateTime) && parseISO(props.day) <= parseISO(endDateTime)) {
        return Bar.renderRectangle(true, {...props, fill:"rgba(225, 225, 225, 0.5)"});
    }
    return <React.Fragment key={props.key}/>;
}

export { formatDay, setBackground }