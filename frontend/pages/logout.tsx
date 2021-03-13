import React, {useEffect} from "react";

export default function logout() {
    useEffect(() => {
        location.assign(`${process.env.NEXT_PUBLIC_BACKEND_URL}/logout`);
    });
    return null;
}
