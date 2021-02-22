import React, { useState, useEffect }  from "react";
import { withStyles, makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { Box } from "@material-ui/core";
import NavBar from "./NavBar";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";

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
    },
}));

// TODO: need to make an API request (backend should have contributors: id, name, etc),
// populate an array of names, sort names alphabetically and display them using mapping

const MenuSideBar = () => {
    const classes = useStyles();
    const router = useRouter();
    const { projectId } =  router.query;
    const PROJECT_ID_URL = `${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/members`;

    const [gitLabMemberName, setGitLabMemberName] = React.useState<[]>([]);

    useEffect(() => {
        if (router.isReady) {
            axios
                .get(PROJECT_ID_URL)
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
            <div>
                {gitLabMemberName.map(member => <div><MenuButton variant="contained" disableRipple> {member.name}</MenuButton></div>)}
            </div>
        </Box>
    );
};

export default MenuSideBar;
