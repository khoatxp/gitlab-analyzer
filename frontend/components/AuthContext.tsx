import React from 'react';
import ChildrenProps from "../interfaces/ChildrenProps";

export type UserCredential = {
    username: String,
    password: String,
}

const defaultUserCredential: UserCredential = {
    username: '',
    password: '',
}

export const AuthContext = React.createContext({
    userCredential: defaultUserCredential,
    setUserCredential: null as unknown as Function,
    getAxiosAuthConfig: () => {return {}}
})

export const AuthProvider = ({children}: ChildrenProps) => {
    const [userCredential, setUserCredential] = React.useState(defaultUserCredential)
    const getAxiosAuthConfig = () => {
        return {
            auth: {
                username: userCredential.username,
                password: userCredential.password,
            }
        }
    }

    return (
        <AuthContext.Provider value={{userCredential, setUserCredential, getAxiosAuthConfig}}>
            {children}
        </AuthContext.Provider>
    )
}