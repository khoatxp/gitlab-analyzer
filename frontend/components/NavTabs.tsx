import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import {useRouter} from "next/router";

const useStyles = makeStyles(() => ({
    root: {
        flexGrow: 1,
        backgroundColor: 'white',
    },
}));

export default function NavTabs({tabSelected}: { tabSelected: number }) {
    const router = useRouter();
    const {projectId, startDateTime, endDateTime} = router.query;
    const queryDate = `?startDateTime=${startDateTime ? startDateTime : "2021-01-01T00:00:00-08:00"}&endDateTime=${endDateTime ? endDateTime : "2021-03-21T00:00:00-08:00"}`;

    const classes = useStyles();

    return (
        <div className={classes.root}>
            <AppBar position="static">
                <Tabs
                    variant="fullWidth"
                    value={tabSelected}
                    aria-label="nav tabs"
                >
                    <Tab
                        label="Overview"
                        onClick={() => router.push(`/project/${projectId}/overview${queryDate}`)}
                    />
                    <Tab
                        label="Code"
                        onClick={() => router.push(`/project/${projectId}/code${queryDate}`)}
                    />
                    <Tab
                        label="Notes"
                        onClick={() => router.push(`/project/${projectId}/notes${queryDate}`)}
                    />
                </Tabs>
            </AppBar>
        </div>
    );
}
