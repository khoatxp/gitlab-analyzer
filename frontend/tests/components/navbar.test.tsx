import React from 'react';
import NavBar from '../../components/NavBar';
import {render} from "@testing-library/react";

describe("NavBar", () =>{

    it("Snapshot NavBar", () => {
        const { container } = render(
            <NavBar />
        )
        expect(container).toMatchSnapshot();
    })

})