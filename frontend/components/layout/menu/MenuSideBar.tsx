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
    const [gitLabMemberNames, setGitLabMemberNames] = React.useState<GitManagementUser[]>([]);
    const [sidebarState, setSidebarState] = React.useState(false);

    const {projectId, gitManagementUserId, startDateTime, endDateTime} = router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/${projectId}/managementusers/members`;

    const handleClick = (id: number) => {
        setActive(id);
        const route = router.route;
        router.push({
            pathname: route,
            query: {projectId: projectId, gitManagementUserId: id, startDateTime: startDateTime, endDateTime: endDateTime}
        })
    };

    const setActive = (id: number | string) => {
        let prevSelected = document.getElementsByClassName('selected');
        prevSelected[0]?.classList.remove('selected');
        document.getElementById(`memberButton${id}`)?.classList.add('selected');
    }


    useEffect(() => {
        if (router.isReady) {
            axios
                .get(PROJECT_ID_URL, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitLabMemberNames(resp.data);
                 }).catch((err: AxiosError)=>{
                    enqueueSnackbar(`Failed to get members: ${err.message}`, {variant: 'error',});
            })
            setActive(Array.isArray(gitManagementUserId) || gitManagementUserId == undefined? "0" : gitManagementUserId)
        }
    }, [projectId]);

    const showSidebar = () => setSidebarState(!sidebarState);

    return (
        <Box width={sidebarState ? '16%' : '3%'} >
            <AppBar position="static" className >
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
            <Box className={`${classes.sidebar} ${sidebarState === true && classes.displaySidebar}`} >
                <MenuButton variant="contained" id={'memberButton0'} disableRipple onClick={() => handleClick(0)}>
                    Everyone
                </MenuButton>
                {gitLabMemberNames.map(member => {
                    const {name} = member;
                    const {id} = member;
                    return <MenuButton key={name} id={`memberButton${{id}.id}`} variant="contained" disableRipple onClick={() => handleClick(+{id}.id)}>{name}</MenuButton>;
                })}
            </Box>
        </Box>
    );
};

export default MenuSideBar;
