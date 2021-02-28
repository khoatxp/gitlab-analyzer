import React, {useEffect, useState} from "react";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import {Divider, IconButton, LinearProgress, Typography} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import ScoreProfile from "../interfaces/ScoreProfile";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import {makeStyles} from "@material-ui/core/styles";


const useStyles = makeStyles(theme => ({
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120
    },
    selectEmpty: {
        marginTop: theme.spacing(2)
    }
}));

const ScoreProfileSelector = () => {

    const classes = useStyles();
    const[profile, setProfile] =  useState<ScoreProfile[]>([]);
    const[selectedProfile, setSelectedProfile] = useState<number>()
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const router = useRouter();
    const {serverId} = router.query;
    const [isIconVisibile, setIconVisibile] = React.useState(false);
    const inputLabel = React.useRef(null);
    const [labelWidth, setLabelWidth] = React.useState(0);
    React.useEffect(() => {
        setLabelWidth(inputLabel.current.offsetWidth);
    }, []);


    useEffect(() => {
        if (router.isReady) {
            axios
                .get(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/scoreprofile/profiles`)
                .then((resp: AxiosResponse) => {
                    setProfile(resp.data);
                    setIsLoading(false);
                });
            ;
        }
    });

    const onProfileSelect = (_event: any, value: ScoreProfile) => {
        setSelectedProfile(value.id);
    }

    const LoadingBar = () => {
        return <div>
            <Typography variant={"body1"}>
                Loading profiles...
            </Typography>
            <LinearProgress/>
        </div>;
    }

    let loadingBar = null;
    if (isLoading) {
        loadingBar = <LoadingBar/>;
    }

    return(
        <div>
            <FormControl variant="outlined" className={classes.formControl}>
                <InputLabel ref={inputLabel} id="scoreOptions">
                    Score Options
                </InputLabel>
                <Select
                    labelId="score-option"
                    id="scoreOptions"
                    value={selectedProfile}
                    onChange={onProfileSelect}
                    labelWidth={labelWidth}
                >
                    {profile.map(item => {
                        return (
                            <MenuItem value={item}>
                                <li>{item.name}</li>
                            </MenuItem>
                        );
                    })}
                </Select>
            </FormControl>
        </div>

    )
}

export default ScoreProfileSelector;