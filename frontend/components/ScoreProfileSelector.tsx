import React from 'react';
import Select, { components } from "react-select";
import {Divider, IconButton} from "@material-ui/core";
import DeleteIcon from "@material-ui/icons/Delete";
import axios, {AxiosResponse} from "axios";

const ScoreProfileSelector = ({options}) => {

    const[profile, setProfile] =  React.useState('');
    const [isIconVisibile, setIconVisibile] = React.useState(false);

    const handleChange = (event) => {
        setProfile(event.target.value);
    };
    
    axios.get("https://localhost:8080/api/v1/scoreprofile/profiles").then((res))
}

    return(
        <div className='selector'>
            <Select
                placeholder="Choose score profile"
                mode="single"
                value={profile}
                onChage{handleChange}
                onOpen={() => setIconVisibile(true)}
                onClose={() => setIconVisibile(false)}
                dropdownRender={menu => (
                    <div>
                        {menu}
                        <Divider style={{ margin: '4px 0' }} />
                        <div
                            style={{ padding: '4px 8px', cursor: 'pointer' }}
                            onMouseDown={e => e.preventDefault()}
                            onClick={this.openModal.bind(this)}
                        >
                            <IconButton type="plus" /> Add Profile
                        </div>
                    </div>
                )}
            >
                {dbConfigList.map(item => (
                    <Option key={item}>{item}

                        <ListItemSecondaryAction variant="outlined">
                            <IconButton edge="end" aria-label="delete">
                                <DeleteIcon />
                            </IconButton>
                        </ListItemSecondaryAction>

                            <Icon
                            onClick={this.deleteFun.bind(this)}
                            type="delete"
                            style={{ fontSize: "20px", color: "#CC160B" }}
                            theme="outlined"
                            /> : null}
                    </Option>

                ))}
            />
        </div>
    )
}

export default ScoreProfile;