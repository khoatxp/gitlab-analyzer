import React from 'react';
import NavBar from '../../components/NavBar';
import {mount} from "enzyme";


describe("NavBar", () =>{

    it("Snapshot NavBar", async() => {
        const rend = mount(
            <NavBar />
        )
        expect(rend).toMatchSnapshot();
    })

})