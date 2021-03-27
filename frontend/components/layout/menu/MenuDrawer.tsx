import React from 'react';
import clsx from 'clsx';
import { makeStyles, useTheme, Theme, createStyles } from '@material-ui/core/styles';
import {Box, Button, Icon } from "@material-ui/core";
import Drawer from '@material-ui/core/Drawer';
import CssBaseline from '@material-ui/core/CssBaseline';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';
import {MenuButton} from "./MenuButton";

const drawerWidth = 240;

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      display: 'flex',
    },
    hide: {
      display: 'none',
    },
    drawer: {
      background: theme.palette.primary.main,
    },
    drawerHeader: {
      width: '10vh',
      alignItems: 'center',
      background: theme.palette.primary.main,
      padding: theme.spacing(0, 1),
      // necessary for content to be below app bar
      ...theme.mixins.toolbar,
      justifyContent: 'center',
    },
    drawerPaper: {
      width: drawerWidth,
    },
    content: {
      flexGrow: 1,
      padding: theme.spacing(3),
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      marginLeft: -drawerWidth,
    },
    contentShift: {
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
      marginLeft: 0,
    },
    background: {
        background: theme.palette.primary.main,
        overflow: 'auto',
    },
  }),
);

const MenuSideBar = () => {
  const classes = useStyles();
  const theme = useTheme();
  const [open, setOpen] = React.useState(false);

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  return (
    <>
        <div className={classes.drawerHeader}>
            <IconButton onClick={handleDrawerOpen}>
                <MenuIcon />
            </IconButton>
        </div>
        <Drawer
            className={classes.drawer}
            variant="persistent"
            anchor="left"
            open={open}
            classes={{
                paper: classes.drawerPaper,
            }}
        >
            <IconButton onClick={handleDrawerClose}>
                {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
            </IconButton>
            <MenuButton variant="contained" disableRipple>
                Everyone
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                Eris Test
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                hannah
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                mahek
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                ida
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                ben
            </MenuButton>
            <MenuButton variant="contained" disableRipple>
                maya
            </MenuButton>
        </Drawer>
</>
  );
}
export default MenuSideBar;
