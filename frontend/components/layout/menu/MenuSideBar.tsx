import React, {useEffect} from "react";
import {makeStyles} from "@material-ui/core/styles";
import {Box} from "@material-ui/core";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {AuthContext} from "../../AuthContext";
import {MenuButton} from "./MenuButton";

const useStyles = makeStyles((theme) => ({
    background: {
        background: theme.palette.primary.main,
        overflow: 'auto',
    },
}));

const MenuSideBar = () => {
    const router = useRouter();
    const classes = useStyles();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [gitLabMemberNames, setGitLabMemberNames] = React.useState<[]>([]);

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

    return (
        <Box
            className={classes.background}
            height="calc(100vh - 7vh)" // 7vh is the height of the NavBar
            width="16vw"
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
    );
};

export default MenuSideBar;
