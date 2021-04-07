import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";
import React from "react";
import AppButton from "./app/AppButton";

type DialogProps = {
    title: string,
    content: string,
    confirmLabel: string,
    open: boolean
    handleClose: () => void
    handleConfirm: () => void
}

const ConfirmationDialog = ({title, content, confirmLabel, open, handleClose, handleConfirm}: DialogProps) => {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">{title}</DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {content}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <AppButton onClick={handleClose} size="medium">
                    Cancel
                </AppButton>
                <AppButton onClick={handleConfirm} size="medium" color="primary" autoFocus>
                    {confirmLabel}
                </AppButton>
            </DialogActions>
        </Dialog>
    );
}

export default ConfirmationDialog;
