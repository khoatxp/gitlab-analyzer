import React from 'react';
import ChildrenProps from "../interfaces/ChildrenProps";

export type UserCredential = {
    username: String,
    password: String,
}

export const defaultUserCredential: UserCredential = {
    username: '',
    password: '',
}

export const AuthContext = React.createContext({
    userCredential: defaultUserCredential,
    setUserCredential: null as unknown as Function,
    getAxiosAuthConfig: () => {return {withCredentials:true}}
})

export const AuthProvider = ({children}: ChildrenProps) => {
    const [userCredential, setUserCredential] = React.useState(defaultUserCredential)
    const getAxiosAuthConfig = () => {
        return {
            withCredentials: true,
        }
    }

    return (
        <AuthContext.Provider value={{userCredential, setUserCredential, getAxiosAuthConfig}}>
            {children}
        </AuthContext.Provider>
    )
}