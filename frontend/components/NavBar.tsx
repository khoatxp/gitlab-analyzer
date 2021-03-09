import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Image from "next/image";
import {Icon} from "@material-ui/core";
import {useRouter} from "next/router";
import {AuthContext, defaultUserCredential} from "./AuthContext";
import AppButton from "./app/AppButton";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        height: '7vh', // TODO: store in variable
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
    const {user} = React.useContext(AuthContext);
    const classes = useStyles();

    const handleLogout = () => {
        location.assign(`${process.env.NEXT_PUBLIC_BACKEND_URL}/logout`);
    }

    return (
        <AppBar className={classes.root} position="static" color="default">
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
                <Typography>Hello {user.username}</Typography>
                <AppButton color="primary" onClick={handleLogout}>Logout</AppButton>
            </Toolbar>
        </AppBar>
    );
};

export default NavBar;
