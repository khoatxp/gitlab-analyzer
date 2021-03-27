import {Box, Typography, Link, Icon} from "@material-ui/core";
import React from "react";
import Image from "next/image";
import AppGradientBackground from "../app/AppGradientBackground";
import ChildrenProps from "../../interfaces/ChildrenProps";
import NextLink from 'next/link'
import {makeStyles} from "@material-ui/styles";

type CardLayoutProps =  ChildrenProps & {
    size?: "sm" | "md"
    logoType?: "center" | "header"
    backLink?: string
    backLabel?: string
}

const useStyles = makeStyles({
    linkBack: {
        display: 'flex',
        alignItems: 'center',
        flexWrap: 'wrap'
    }
})

const CardLayout = ({children, size, logoType = "center", backLink, backLabel}: CardLayoutProps) => {
    // Change card width based on size prop
    let width = "60vw";
    width = size == "sm" ? "20vw": width;
    width = size == "md" ? "60vw": width;

    let logoSize = logoType === "center" ? "120" : "80" ;
    const classes = useStyles();

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
                        <NextLink href={backLink} passHref>
                            <Link classes={{root:classes.linkBack}}>
                                <Icon fontSize="small">arrow_back</Icon>
                                <Typography variant="button"> {backLabel ? backLabel : 'back' }</Typography>
                            </Link>
                        </NextLink>
                    </Box>
                }
            </Box>
        </AppGradientBackground>
    );
};

export default CardLayout;
