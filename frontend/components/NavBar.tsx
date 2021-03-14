import React from "react";
import {makeStyles} from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Image from "next/image";
import {Box, Button, Icon, Link, Menu, MenuItem} from "@material-ui/core";
import {AuthContext} from "./AuthContext";
import NextLink from "next/link";
import {useRouter} from "next/router";

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        height: '7vh', // TODO: store in variable
    },
    menuButton: {
        marginRight: theme.spacing(3),
    },
    title: {
        paddingLeft: "0.75em",
        flexGrow: 1,
    },
    user: {
        display: "flex",
        alignItems: "center"
    },
    userIcon: {
        marginRight: "5px",
    },
    buttonText: {
        textTransform: 'none',
    }
}));

const NavBar = () => {
    const {user} = React.useContext(AuthContext);
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const router = useRouter();
    const { projectId } =  router.query;
    const classes = useStyles();

    const handleClickMenu = (event: React.MouseEvent<HTMLButtonElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };


    const handleLogout = () => {
        location.assign(`${process.env.NEXT_PUBLIC_BACKEND_URL}/logout`);
    }

    return (
        <AppBar className={classes.root} position="sticky" color="default">
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
                <Button
                    aria-controls="config-menu"
                    aria-haspopup="true"
                    onClick={handleClickMenu}
                    className={classes.buttonText}
                >
                    <Typography className={classes.user} variant="h6">
                        <Icon className={classes.userIcon}>account_circle</Icon>
                        {user ? user.username : ''}
                    </Typography>
                    <Box display="flex" alignItems="center" marginLeft="1em">
                        <Icon>settings</Icon>
                    </Box>
                </Button>
                <Menu
                    id="setting-menu"
                    anchorEl={anchorEl}
                    getContentAnchorEl={null}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                    anchorOrigin={{vertical: "bottom", horizontal: "center"}}
                    transformOrigin={{vertical: "top", horizontal: "center"}}
                >
                    <MenuItem onClick={handleClose}>
                        <NextLink href="/server" passHref>
                            <Link>
                                Servers
                            </Link>
                        </NextLink>
                    </MenuItem>
                    <MenuItem onClick={handleLogout}>Logout</MenuItem>
                </Menu>
                    </Button>
                    <Menu
                        id="setting-menu"
                        anchorEl={anchorEl}
                        getContentAnchorEl={null}
                        keepMounted
                        open={Boolean(anchorEl)}
                        onClose={handleClose}
                        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
                        transformOrigin={{ vertical: "top", horizontal: "center" }}
                    >
                        <MenuItem onClick={handleClose}>
                            <NextLink href="/server" passHref>
                                <Link>
                                    Servers
                                </Link>
                            </NextLink>

                        </MenuItem>
                        {projectId?
                            <MenuItem onClick={handleClose}>
                                <NextLink href={`/project/${projectId}/members`}>
                                    <Link>
                                        Members
                                    </Link>
                                </NextLink>
                            </MenuItem>:''
                        }
                        <MenuItem onClick={handleLogout}>Logout</MenuItem>
                    </Menu>
                </div>
                {/*<AppButton color="primary" size="medium" onClick={handleLogout}>Logout</AppButton>*/}
            </Toolbar>
        </AppBar>
    );
};

export default NavBar;
