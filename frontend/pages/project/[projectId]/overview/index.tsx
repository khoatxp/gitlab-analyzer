import React from "react";
import NavBar from "../../../../components/NavBar";
import MenuSideBar from "../../../../components/MenuSideBar";
import NavTabs from "../../../../components/NavTabs";
import CodeAnalysis from "../../../../components/CodeAnalysis";
import {Box} from "@material-ui/core";
import AuthView from "../../../../components/AuthView";

const index = () => {
    return (
        <AuthView>
            <NavBar/>
            <NavTabs tabSelected={0}/>
            <Box display="flex">
                <MenuSideBar/>
                <CodeAnalysis/>
            </Box>
        </AuthView>
    );
};

export default index;
