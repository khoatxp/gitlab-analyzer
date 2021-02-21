import React from "react";
import NavBar from "../../components/NavBar";
import MenuSideBar from "../../components/MenuSideBar";
import NavTabs from "../../components/NavTabs";
import MenuTabs from "../../components/MenuTabs";
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    contentContainer: {
      display: 'flex',
      width: '100%',
    },
  }),
);

const index = () => {
    const classes = useStyles();
    return (
        <>
            <NavBar/>
            <div className={classes.contentContainer}>
                <MenuSideBar/>
                <NavTabs/>
            </div>
        </>
    );
};

export default index;
