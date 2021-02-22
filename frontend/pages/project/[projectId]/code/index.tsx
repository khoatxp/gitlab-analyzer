import React from "react";
import NavBar from "../../../../components/NavBar";
import MenuSideBar from "../../../../components/MenuSideBar";
import NavTabs from "../../../../components/NavTabs";
import { makeStyles, createStyles } from '@material-ui/core/styles';
import CodeAnalysis from "../../../../components/CodeAnalysis";
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
            <NavTabs tabSelected={0}/>
            <Box className={classes.contentContainer}>
                <MenuSideBar/>
                <CodeAnalysis/>
            </Box>
        </AuthView>
    );
};

export default index;
