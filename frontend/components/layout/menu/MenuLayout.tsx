import React from "react";
import NavBar from "../../NavBar";
import NavTabs from "./NavTabs";
import {Box} from "@material-ui/core";
import MenuSideBar from "./MenuSideBar";
import ChildrenProps from "../../../interfaces/ChildrenProps";

type AnalysisLayoutProps = {tabSelected: number} & ChildrenProps

const index = ({tabSelected, children}: AnalysisLayoutProps) => {
    return (
        <>
            <NavBar/>
            <Box display="flex">
                <MenuSideBar/>
                <Box width='100%'>
                    <NavTabs tabSelected={tabSelected}/>
                    <Box padding="2em">
                        {children}
                    </Box>
                </Box>
            </Box>
        </>
    );
};

export default index;
