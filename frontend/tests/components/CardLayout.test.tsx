
import React, {ReactNode} from 'react';
import CardLayout from '../../components/CardLayout';
import {render} from "@testing-library/react";



describe("Cardlayout", () =>{
    // Dummy child to render CardLayout
    const children: ReactNode = <div/>

    it("Snapshot CardLayout", () => {
        const { container } = render(
            <CardLayout children={children}/>
        )
        expect(container).toMatchSnapshot();
    })

})