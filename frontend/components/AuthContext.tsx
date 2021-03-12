import React from 'react';
import ChildrenProps from "../interfaces/ChildrenProps";

export type User = {
    id: Number,
    username: String,
}

export type AuthContextType = {
    user: User | null,
    setUser: Function,
    getAxiosAuthConfig: Function
}

export const AuthContext = React.createContext<AuthContextType>({
    user: null,
    setUser: null as unknown as Function,
    getAxiosAuthConfig: () => {return {withCredentials:true}}
})

export const AuthProvider = ({children}: ChildrenProps) => {
    const [user, setUser] = React.useState(null)
    const getAxiosAuthConfig = () => {
        return {
            withCredentials: true,
        }
    }

    return (
        <AuthContext.Provider value={{user, setUser, getAxiosAuthConfig}}>
            {children}
        </AuthContext.Provider>
    )
}