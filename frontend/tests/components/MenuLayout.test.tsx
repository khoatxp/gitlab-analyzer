import React, {ReactNode} from 'react';
import MenuLayout from '../../components/layout/menu/MenuLayout';
import {render} from "@testing-library/react";



describe("MenuLayout", () =>{
    // Dummy child to render CardLayout
    const children: ReactNode = <div/>;

    it("Snapshot MenuLayout", () => {
        const { container } = render(
            <MenuLayout tabSelected={0} children={children}/>
        )
        expect(container).toMatchSnapshot();
    });

})