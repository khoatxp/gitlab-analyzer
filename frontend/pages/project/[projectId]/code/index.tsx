import React from "react";
import NavBar from "../../../../components/NavBar";
import MenuSideBar from "../../../../components/MenuSideBar";
import NavTabs from "../../../../components/NavTabs";
import { makeStyles, createStyles } from '@material-ui/core/styles';
import CodeAnalysis from "../../../../components/CodeAnalysis";
import {Box} from "@material-ui/core";

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
        <>
            <NavBar/>
            <NavTabs tabSelected={0}/>
            <Box className={classes.contentContainer}>
                <MenuSideBar/>
                <CodeAnalysis/>
            </Box>
        </>
    );
};

export default index;
