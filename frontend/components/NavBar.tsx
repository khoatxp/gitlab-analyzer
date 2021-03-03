import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Image from "next/image";
import {Icon} from "@material-ui/core";
import {useRouter} from "next/router";
import { defaultUserCredential, AuthContext } from "./AuthContext";
import AppButton from "./AppButton";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        paddingLeft: "0.75em",
        flexGrow: 1,
    },
}));

const NavBar = () => {
    const router = useRouter();
    const { setUserCredential } = React.useContext(AuthContext);
    const classes = useStyles();

    const handleLogout = () => {
        setUserCredential(defaultUserCredential);
        router.push('/login');
    }

    return (
        <div className={classes.root}>
            <AppBar position="static" color="default">
                <Toolbar>
                    <Icon>
                        <Image
                            src="/gitlab.svg"
                            alt="The Gitlab Logo"
                            width={100}
                            height={100}
                        />
                    </Icon>
                    <Typography variant="h6" className={classes.title}>
                        Gitlab Analyzer
                    </Typography>
                    <AppButton color="primary" onClick={handleLogout}>Logout</AppButton>
                </Toolbar>
            </AppBar>
        </div>
    );
};

export default NavBar;
