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
    const classes = useStyles();
    const router = useRouter();

    const {projectId, startDateTime, endDateTime} = router.query;
    const queryStartDateTime = startDateTime ? startDateTime : "2021-01-01T00:00:00-08:00";
    const queryEndDateTime = endDateTime ? endDateTime : "2021-03-21T00:00:00-08:00";
    const queryDate = `?startDateTime=${queryStartDateTime}&endDateTime=${queryEndDateTime}`;

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
                    <Tab
                        label="Members"
                        onClick={() => router.push(`/project/${projectId}/members`)}
                    />
                </Tabs>
            </AppBar>
        </div>
    );
}
