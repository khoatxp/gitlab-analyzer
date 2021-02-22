import React from "react";
import NavBar from "../../../../components/NavBar";
import MenuSideBar from "../../../../components/MenuSideBar";
import NavTabs from "../../../../components/NavTabs";
import { makeStyles, createStyles } from '@material-ui/core/styles';
import AuthView from "../../../../components/AuthView";

const useStyles = makeStyles(() =>
  createStyles({
    contentContainer: {
      display: 'flex',
      width: '100%',
      padding: '24px',
    },
  }),
);

const index = () => {
    const classes = useStyles();
    return (
        <AuthView>
            <NavBar/>
            <div className={classes.contentContainer}>
                <MenuSideBar/>
                <NavTabs/>
            </div>
        </AuthView>
    );
};

export default index;
