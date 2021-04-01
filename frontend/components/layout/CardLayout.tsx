import {Box, Typography, Link, Icon} from "@material-ui/core";
import React from "react";
import Image from "next/image";
import AppGradientBackground from "../app/AppGradientBackground";
import ChildrenProps from "../../interfaces/ChildrenProps";
import AppButton from "../app/AppButton";
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import {useRouter} from "next/router";

type CardLayoutProps = ChildrenProps & {
    size?: "sm" | "md" | "lg"
    logoType?: "center" | "header"
    backLink?: string
    backLabel?: string
}

const CardLayout = ({children, size, logoType = "center", backLink, backLabel}: CardLayoutProps) => {
    const router = useRouter();

    // Change card width based on size prop
    let width = "60vw";
    width = size == "sm" ? "20vw" : width;
    width = size == "md" ? "60vw" : width;
    width = size == "lg" ? "80vw" : width;

    let logoSize = logoType === "center" ? "120" : "80";

    return (
        <AppGradientBackground>
            <Box
                bgcolor="primary.contrastText"
                boxShadow={20}
                width={width}
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
                <Box
                    flex= {logoType === "center" ? "none" : 1}
                    display="flex"
                    flexDirection="column"
                    justifyContent="center"
                >
                    {children}
                </Box>
                {backLink &&
                <Box>
                    <AppButton
                        startIcon={<ArrowBackIcon/>}
                        onClick={() => router.push(backLink)}
                    >
                        {backLabel ? backLabel : 'back'}
                    </AppButton>
                </Box>
                }
            </Box>
        </AppGradientBackground>
    );
};

export default CardLayout;
