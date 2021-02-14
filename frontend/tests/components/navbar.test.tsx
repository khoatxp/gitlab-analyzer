import React from 'react';
import NavbBar from '../../components/NavBar';
import {render} from "@testing-library/react";

describe("NavBar", () =>{

    it("Snapshot NavBar", () => {
        const { container } = render(
            <NavbBar />
        )
        expect(container).toMatchSnapshot();
    })

})