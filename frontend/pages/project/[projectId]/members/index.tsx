import React from "react";
import NavBar from "../../../../components/NavBar";
import MemberMapping from "../../../../components/MemberMapping";
import {Box} from "@material-ui/core";
import AuthView from "../../../../components/AuthView";

const index = () => {
    return (
        <AuthView>
            <NavBar/>
            <Box display="flex">
                <MemberMapping/>
            </Box>
        </AuthView>
    );
};

export default index;