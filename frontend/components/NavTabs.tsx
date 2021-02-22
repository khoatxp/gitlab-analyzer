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
    const {projectId} = router.query;

    const classes = useStyles();

    return (
        <div className={classes.root}>
            <AppBar position="static">
                <Tabs
                    variant="fullWidth"
                    value={tabSelected}
                    aria-label="nav tabs"
                >
                    <Tab label="Code" href={`/project/${projectId}/code`}/>
                    <Tab label="Activity" href="#"/>
                    <Tab label="Notes" href={`/project/${projectId}/notes`}/>
                </Tabs>
            </AppBar>
        </div>
    );
}
