import React, {ReactNode} from 'react';
import CardLayout from '../../components/layout/CardLayout';
import {mount} from "enzyme";



describe("Cardlayout", () =>{
    // Dummy child to render CardLayout
    const children: ReactNode = <div/>;

    it("Snapshot CardLayout", async () => {
        const rend = mount(
            <CardLayout children={children}/>
        )
        await Promise.resolve();
        expect(rend).toMatchSnapshot();
    });

})