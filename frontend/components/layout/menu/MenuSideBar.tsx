import React, {useEffect} from "react";
import {makeStyles} from "@material-ui/core/styles";
import {Box, Button, Icon } from "@material-ui/core";
import {useRouter} from "next/router";
import axios, {AxiosError, AxiosResponse} from "axios";
import {AuthContext} from "../../AuthContext";
import {MenuButton} from "./MenuButton";
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import {GitManagementUser} from "../../../interfaces/GitManagementUser";
import {useSnackbar} from "notistack";
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import MenuIcon from '@material-ui/icons/Menu';

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
        '& .MuiTab-wrapper': {
          alignItems: 'flex-start',
        },
    },
}));

const MenuSideBar = () => {
    const router = useRouter();
    const classes = useStyles();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitManagementUsers, setGitManagementUsers] = React.useState<GitManagementUser[]>([]);
    const [sidebarState, setSidebarState] = React.useState(true);

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
                    setGitManagementUsers(resp.data);
                 }).catch((err:AxiosError)=>{
                    enqueueSnackbar(`Failed to get members: ${err}`, {variant: 'error',});
            })
            setActive(Array.isArray(gitManagementUserId) || gitManagementUserId == undefined? "0" : gitManagementUserId)
        }
    }, [projectId]);

    const showSidebar = () => setSidebarState(!sidebarState);


    return (
        <Box width={sidebarState ? '16%' : '3.5%'} >
            <AppBar position="static">
                <Tabs
                    variant="fullWidth"
                    aria-label="nav tabs"
                    alignItems="flex-start"
                >
                    <Tab
                        className={classes.sidebarTitle}
                        onClick={showSidebar}
                        label={sidebarState ? <ChevronLeftIcon /> : <MenuIcon /> }
                    />
                </Tabs>
            </AppBar>
            <Box className={`${classes.sidebar} ${sidebarState === true && classes.displaySidebar}`} >
                <MenuButton variant="contained" id={'memberButton0'} disableRipple onClick={() => handleClick(0)}>
                    Everyone
                </MenuButton>
                {gitManagementUsers.map(gitManagementUser =>
                    <MenuButton key={gitManagementUser.id}
                                value={[gitManagementUser.id.toString(),gitManagementUser.username]}
                                id={`memberButton${gitManagementUser.id}`}
                                variant="contained" disableRipple
                                onClick={() => handleClick(gitManagementUser.id)}
                    >
                        {gitManagementUser.username}
                    </MenuButton>
                )}
            </Box>
        </Box>
    );
};

export default MenuSideBar;
