import React, {useEffect} from "react";
import {makeStyles} from "@material-ui/core/styles";
import {Box, Button, Icon } from "@material-ui/core";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {AuthContext} from "../../AuthContext";
import {MenuButton} from "./MenuButton";
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';

const useStyles = makeStyles((theme) => ({
 background: {
      background: theme.palette.primary.main,
    },
    sidebar: {
     background: theme.palette.primary.main,
        overflow: 'auto',
        position: 'relative',
        left: '-100%',
        transition: '700ms',
    },
    displaySidebar: {
         left: '0',
           transition: '350ms',
    },
    hideBar: {
        display: 'none',
    },
    sidebarTitle: {
        color: "black",
        justifyContent: "flex-start",
        alignItems: 'flex-start',
    },
}));

const MenuSideBar = () => {
    const router = useRouter();
    const classes = useStyles();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitLabMemberNames, setGitLabMemberNames] = React.useState<[]>([]);
    const [sidebarState, setSidebarState] = React.useState(false);

    const {projectId} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/members`;

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(PROJECT_ID_URL, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitLabMemberNames(resp.data);
                });
        }
    }, [projectId]);

    const showSidebar = () => setSidebarState(!sidebarState);

    return (
        <Box
            classes={classes.background}
            width={sidebarState ? '16vw' : '3vw'}
            height="auto"
            display="flex"
            flexDirection="column"
            justifyContent="flex-start"
            alignItems="center"
        >
            <AppBar position="static" >
                <Tabs
                    variant="fullWidth"
                    aria-label="nav tabs"
                >
                    <Tab
                        className={classes.sidebarTitle}
                        onClick={showSidebar}
                        label={sidebarState ? '<' : '>'}
                    />
                </Tabs>
            </AppBar>
            <Box
                className={`${classes.sidebar} ${sidebarState === true && classes.displaySidebar}`}
                height="auto"
                width="100%"
                display="flex"
                flexDirection="column"
                justifyContent="flex-start"
                alignItems="center"
            >
                <MenuButton variant="contained" disableRipple>
                    Everyone
                </MenuButton>
                {gitLabMemberNames.map(member => {
                    const {name} = member;
                    return <MenuButton key={name} variant="contained" disableRipple> {name}</MenuButton>;
                })}
            </Box>
        </Box>
    );
};

export default MenuSideBar;
