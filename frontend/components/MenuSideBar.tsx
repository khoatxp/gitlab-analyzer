import React, { useState, useEffect }  from "react";
import { withStyles, makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { Box } from "@material-ui/core";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {AuthContext} from "./AuthContext";

const MenuButton = withStyles({
    root: {
        textTransform: 'none',
        fontSize: '16px',
        padding: '20px',
        border: '1px solid white',
        color: 'black',
        width: '80%',
        lineHeight: 0.8,
        backgroundColor: 'white',
        borderRadius: '999px',
        margin: '20px 0',
        '&:hover': {
          backgroundColor: '#8FC6F3',
          borderColor: '#8FC6F3',
        },
        '&:active': {
          backgroundColor: 'primary',
          borderColor: '#005cbf',
        },
        '&:focus': {
          boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
        },
    },
})(Button);

const useStyles = makeStyles((theme) => ({
    background: {
        background: theme.palette.primary.main,
        overflow: 'auto',
    },
}));

const MenuSideBar = () => {
    const classes = useStyles();
    const router = useRouter();
    const { projectId } =  router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/members`;
    const [gitLabMemberName, setGitLabMemberName] = React.useState<[]>([]);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(PROJECT_ID_URL, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setGitLabMemberName(resp.data);
                });
        }
    }, [projectId]);

    return (
        <Box
            className={classes.background}
            height="100vh"
            width="16vw"
            display="flex"
            flexDirection="column"
            justifyContent="flex-start"
            alignItems="center"
            >
            <MenuButton variant="contained" disableRipple>
                Everyone
            </MenuButton>
            {gitLabMemberName.map(member => <MenuButton variant="contained" disableRipple> {member.name}</MenuButton>)}
        </Box>
    );
};

export default MenuSideBar;
