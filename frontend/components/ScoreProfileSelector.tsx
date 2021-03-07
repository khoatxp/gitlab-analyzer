import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {DeleteIcon, AddBoxIcon, EditIcon} from '@material-ui/icons';
import {makeStyles} from "@material-ui/core/styles";
import {AuthContext } from "./AuthContext";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import FormControl from '@material-ui/core/FormControl';
import DialogContent from "@material-ui/core/DialogContent";
import TextField from "@material-ui/core/TextField";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import Select from '@material-ui/core/Select';


const useStyles = makeStyles(theme => ({  
    formControl: {
        margin: theme.spacing(5),
        minWidth: 150,
    },
    rowAlign:{
        flexDirection: "row",
        justifyContent: "center",
        alignItems:"center",
    }
}));

function Popup(props){
    const classes = useStyles();
    const { open, handleClose, id } = props;
  
    const close = () => {
      handleClose();
    };
  
  
  
    const handleSubmit = e => {
      //();
  
    };
  
    return (
        <React.Fragment>
        <Dialog open={open} onClose={close} fullWidth maxWidth="sm">
          <button onClick={close}>X</button>
          <Typography align="center"> Score Profile</Typography>
            <DialogContent>
              <form className={classes.root} onSubmit={handleSubmit}>
              <div>
                <TextField
                  name="body"
                  label="New Line"
                  placeholder="Weight"
                  //value
                  //onChange={handleChange}        
                />
                </div>
                <div align="end">
                  <button type="submit" variant="contained" color="primary">
                    Save           
                  </button>
                </div>
              </form>
            </DialogContent>
        </Dialog>
      </React.Fragment>
    )
  }
const ScoreProfileSelector = () => {

    const classes = useStyles();
    //const[profiles, setProfiles] =  useState<ScoreProfile[]>([]);
    const profiles = ['a','b','c','d'];
    const [isIconVisibile, setIconVisibile] = React.useState(false);
    const[selectedProfile, setSelectedProfile] = useState<number>()
    const [open, setOpen] = React.useState(false);
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const router = useRouter();
      
 
    /** 
    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile/profiles`, getAxiosAuthConfig())
                .then((resp: AxiosResponse) => {
                    setProfile(resp.data);
                    setIsLoading(false);
                });
            ;
        }
    });
    */

    const onProfileSelect = (_event: any, value: ScoreProfile) => {
        setSelectedProfile(value.id);
    }

    const handleOpen = () => {
        setOpen(true);
      };
    
      const handleClose = () => {
        setOpen(false);
      };
    
    
      return (
        <div className={classes.rowAlign}>        
                <FormControl className={classes.formControl}>
                    <InputLabel id="score-options">Score Options</InputLabel>
                    <Select 
                        labelId="score-options"
                        onOpen={() => setIconVisibile(true)}
                        onClose={() => setIconVisibile(false)}
                    >
                      {profiles.map(p => (
                        <MenuItem value={p}>
                          {p}    
                            {isIconVisibile ? (
                              <ListItemSecondaryAction variant="outlined">
                                <IconButton edge="end" aria-label="edit" onClick={handleOpen}>
                                  <EditIcon style={{ fontSize: "25px", color: "grey" }} />
                                </IconButton>
                                <Popup  open={open} handleClose={handleClose} />
                                <IconButton edge="end" aria-label="delete">
                                  <DeleteIcon style={{ fontSize: "25px", color: "#CC160B" }}/>
                                </IconButton>
                              </ListItemSecondaryAction>
                              ) : null}
                        </MenuItem>
                      ))}
                    </Select>
                </FormControl>
    
                <IconButton edge="start" aria-label="add" onClick={handleOpen}>
                    <AddBoxIcon style={{ fontSize: "25px", color: "green" }}/>
                </IconButton>
                <Popup  open={open} handleClose={handleClose} />
          </div>
      );
    }

export default ScoreProfileSelector;