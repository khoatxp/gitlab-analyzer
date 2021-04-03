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
import {GitManagementUser} from "../interfaces/GitManagementUser";
import {useSnackbar} from "notistack";

const useStyles = makeStyles((theme) => ({
    sidebar: {
        background: theme.palette.primary.main,
        overflow: 'auto',
        position: 'relative',
        left: '-100%',
        transition: '600ms',
             height: "100%",
                        width: "100%",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "flex-start",
                        alignItems: "center",
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
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitLabMemberUserNames, setGitLabMemberUserNames] = React.useState<[]>([]);
    const [sidebarState, setSidebarState] = React.useState(false);

    const {projectId} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/${projectId}/managementusers/members`;

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(PROJECT_ID_URL, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitLabMemberUserNames(resp.data);
                }).catch((err: AxiosError)=>{
                    enqueueSnackbar(`Failed to get members: ${err.message}`, {variant: 'error',});
            })
        }
    }, [projectId]);

    const showSidebar = () => setSidebarState(!sidebarState);

    return (
        <Box

            width={sidebarState ? '16%' : '3%'}
        >
            <AppBar position="static" className >
                <Tabs
                    variant="fullWidth"
                    aria-label="nav tabs"
                >
                    <Tab
                        className={classes.sidebarTitle}
                        onClick={showSidebar}
                        label={sidebarState ? '<' : '>Show    >'}
                    />
                </Tabs>
            </AppBar>
            <Box
                className={`${classes.sidebar} ${sidebarState === true && classes.displaySidebar}`}
            >
                <MenuButton variant="contained" disableRipple >
                    Everyone
                </MenuButton>
                <MenuButton variant="contained" disableRipple >
                    mahekk
                </MenuButton>
                <MenuButton variant="contained" disableRipple >
                    idanok
                </MenuButton>
                <MenuButton variant="contained" disableRipple >
                    aturner
                </MenuButton>
                {gitLabMemberUserNames.map(member => {
                    const {userName} = member;
                    return <MenuButton key={userName} variant="contained" disableRipple > {userName}</MenuButton>;
                })}

            </Box>
        </Box>
    );
};

export default MenuSideBar;
