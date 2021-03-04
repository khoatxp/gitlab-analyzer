import {Box, Typography} from "@material-ui/core";
import React from "react";
import Image from "next/image";
import AppGradientBackground from "./AppGradientBackground";
import ChildrenProps from "../interfaces/ChildrenProps";

type CardLayoutProps =  ChildrenProps & {
    size?: "sm" | "md"
    smLogo?: boolean
}

const CardLayout = ({children, size, smLogo}: CardLayoutProps) => {
    // Change card width based on size prop
    let width = "60vw";
    width = size == "sm" ? "20vw": width;
    width = size == "md" ? "60vw": width;

    let logoSize = smLogo ? "50": "80";

    return (
        <AppGradientBackground>
            <Box
                bgcolor="primary.contrastText"
                boxShadow={20}
                width={width}
                height="60vh"
                minWidth="250px"
                minHeight="450px"
                borderRadius={45}
                padding="20px"
                display="flex"
                flexDirection="column"
                justifyContent="center"
            >
                <Box id="card-header" margin="10px" textAlign="center">
                    <Image
                        src="/gitlab.svg"
                        alt="The Gitlab Logo"
                        width={logoSize}
                        height={logoSize}
                    />
                    <Typography variant="h6">
                        Gitlab Analyzer
                    </Typography>
                </Box>
                {children}
            </Box>
        </AppGradientBackground>
    );
};

export default CardLayout;
