import React, {useState} from "react";
import {parseISO} from "date-fns";
import {Bar} from "recharts";
import axios, {AxiosResponse} from "axios";
import {useSnackbar} from "notistack";
import {AuthContext} from "../AuthContext";



const formatDay = (day:string) => {
    return parseISO(day).toLocaleDateString('en-Us', {month: 'short', day: 'numeric'});
}

const setBackground = (props:any, startDateTime:string | undefined, endDateTime:string | undefined) => {
    if ((startDateTime === undefined || parseISO(props.day) >= parseISO(startDateTime)) &&
        (endDateTime === undefined ||parseISO(props.day) <= parseISO(endDateTime))) {
        return Bar.renderRectangle(true, {...props, fill:"rgba(225, 225, 225, 0.5)"});
    }
    return <React.Fragment key={props.key}/>;
}

export { formatDay, setBackground }