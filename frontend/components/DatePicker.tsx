import 'date-fns';
import React from 'react';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, KeyboardDatePicker} from '@material-ui/pickers';


const DatePicker = () => {

    const [startDate, setStartDate] = React.useState<Date | null>(new Date('01-02-2021'));
    const [endDate, setEndDate] =  React.useState<Date | null>(new Date('01-05-2021'));

    const handleStartChange = (date: Date | null) => {
        setStartDate(date);
    };

    const handleEndChange = (date: Date | null) => {
        setEndDate(date);
    };

    return(
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
            <Grid container justify="space-around">
                <KeyboardDatePicker
                    disableToolbar
                    variant="inline"
                    format="dd/MM/yyyy"
                    margin="normal"
                    id="start-date-picker"
                    label="Start Date:"
                    value={startDate}
                    onChange={handleStartChange}
                    autoOk={true}
                    KeyboardButtonProps={{
                        'aria-label': 'change date',
                    }}

                />

                <KeyboardDatePicker
                    disableToolbar
                    variant="inline"
                    format="dd/MM/yyyy"
                    margin="normal"
                    id="end-date-picker"
                    label="End Date:"
                    value={endDate}
                    onChange={handleEndChange}
                    autoOk={true}
                    KeyboardButtonProps={{
                        'aria-label': 'change date',
                    }}
                />
            </Grid>
        </MuiPickersUtilsProvider>

    )

}

export default DatePicker;