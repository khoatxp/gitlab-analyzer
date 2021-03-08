import 'date-fns';
import React from 'react';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, DateTimePicker} from '@material-ui/pickers';

interface Props {
    onStartDateTimeChange: (x:Date) => void
    onEndDateTimeChange: (x:Date) => void
    startDateTime: Date,
    endDateTime: Date
}

const AppDateTimePicker = ({onStartDateTimeChange, onEndDateTimeChange, startDateTime, endDateTime}:Props) => {
    return(
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Grid container justify="space-around">
                <DateTimePicker
                    variant="inline"
                    format="dd/MM/yyyy HH:mm"
                    margin="normal"
                    id="start-date-picker"
                    label="Start Date:"
                    value={startDateTime}
                    helperText='day/month/year hour:minute'
                    onChange={onStartDateTimeChange}
                    autoOk={true}

                />

                <DateTimePicker
                    variant="inline"
                    format="dd/MM/yyyy HH:mm"
                    margin="normal"
                    id="end-date-picker"
                    label="End Date:"
                    value={endDateTime}
                    helperText='day/month/year hour:minute'
                    onChange={onEndDateTimeChange}
                    autoOk={true}

                />
            </Grid>
        </MuiPickersUtilsProvider>

    )

}

export default AppDateTimePicker;