import 'date-fns';
import React,{useState} from 'react';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, DateTimePicker} from '@material-ui/pickers';


const DatePicker = () => {

    var now = new Date();
    const [startDate, setStartDate] = React.useState<Date>(new Date(now.getFullYear(),now.getMonth()-1, now.getDate()));
    const [endDate, setEndDate] =  React.useState<Date>(new Date());

    return(
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Grid container justify="space-around">
                <DateTimePicker
                    variant="inline"
                    format="dd/MM/yyyy HH:mm"
                    margin="normal"
                    id="start-date-picker"
                    label="Start Date:"
                    value={startDate}
                    helperText='day/month/year hour:minute'
                    onChange={setStartDate}
                    autoOk={true}

                />

                <DateTimePicker
                    variant="inline"
                    format="dd/MM/yyyy HH:mm"
                    margin="normal"
                    id="end-date-picker"
                    label="End Date:"
                    value={endDate}
                    helperText='day/month/year hour:minute'
                    onChange={setEndDate}
                    autoOk={true}

                />
            </Grid>
        </MuiPickersUtilsProvider>

    )

}

export default DatePicker;