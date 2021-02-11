import {Box, Typography} from "@material-ui/core";
import React from "react";
import Image from "next/image";
import AppGradientBackground from "./AppGradientBackground";
import ChildrenProps from "../interfaces/ChildrenProps";

type CardLayoutProps =  ChildrenProps & {size?: "sm" | "md"}

const CardLayout = ({children, size}: CardLayoutProps) => {
    // Change card width based on size prop
    let width = "60vw";
    width = size == "sm" ? "20vw": width;
    width = size == "md" ? "60vw": width;

    return (
        <AppGradientBackground>
            <Box
                bgcolor="primary.contrastText"
                boxShadow={20}
                width={width}
                height="65vh"
                minWidth="250px"
                minHeight="400px"
                borderRadius={45}
                padding="20px"
                display="flex"
                flexDirection="column"
                justifyContent="center"
            >
                <Image
                    src="/gitlab.svg"
                    alt="The Gitlab Logo"
                    width={100}
                    height={100}
                />
                <Typography variant="h6" align="center" gutterBottom>GitLab<br/>Analyzer</Typography>
                {children}
            </Box>
        </AppGradientBackground>
    );
};

export default CardLayout;
