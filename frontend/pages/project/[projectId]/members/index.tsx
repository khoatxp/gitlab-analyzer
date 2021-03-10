import React from "react";
import NavBar from "../../../../components/NavBar";
import NavTabs from "../../../../components/layout/menu/NavTabs";
import { makeStyles, createStyles } from '@material-ui/core/styles';
import MemberMapping from "../../../../components/MemberMapping";
import {Box} from "@material-ui/core";
import AuthView from "../../../../components/AuthView";

const useStyles = makeStyles(() =>
    createStyles({
        contentContainer: {
            display: 'flex',
        },
    }),
);

const index = () => {
    const classes = useStyles();
    return (
        <AuthView>
            <NavBar/>
            <NavTabs tabSelected={3}/>
            <Box className={classes.contentContainer}>
                <MemberMapping/>
            </Box>
        </AuthView>
    );
};

export default index;