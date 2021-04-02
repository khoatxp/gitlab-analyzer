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
                });
            setActive(Array.isArray(gitManagementUserId) || gitManagementUserId == undefined? "0" : gitManagementUserId)
        }
    }, [projectId]);

    return (
        <Box
            className={classes.background}
            height="auto"
            width="16vw"
            display="flex"
            flexDirection="column"
            justifyContent="flex-start"
            alignItems="center"
        >
            <MenuButton variant="contained" id={'memberButton0'} disableRipple onClick={() => handleClick(0)}>
                Everyone
            </MenuButton>
            {gitLabMemberNames.map(member => {
                const {name} = member;
                const {id} = member;
                return <MenuButton key={name} id={`memberButton${{id}.id}`} variant="contained" disableRipple onClick={() => handleClick(+{id}.id)}>{name}</MenuButton>;
            })}
        </Box>
    );
};

export default MenuSideBar;
