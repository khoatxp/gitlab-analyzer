import React from 'react';
import ChildrenProps from "../interfaces/ChildrenProps";

export type SideBarContextType = {
    isSideBarOpen: boolean,
    setIsSideBarOpen: Function,
}

export const SideBarContext = React.createContext<SideBarContextType>({
    isSideBarOpen: true,
    setIsSideBarOpen: () => {},
})

export const SideBarProvider = ({children}: ChildrenProps) => {
    const [isSideBarOpen, setIsSideBarOpen] = React.useState(true);

    return (
        <SideBarContext.Provider value={{isSideBarOpen, setIsSideBarOpen}}>
            {children}
        </SideBarContext.Provider>
    )
}