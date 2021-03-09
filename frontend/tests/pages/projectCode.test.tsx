import React from 'react';

import Index from '../../pages/project/[projectId]/code';
import {mount} from "enzyme";


describe("Pages Project Code", () =>{

    it("Snapshot Index", async() => {
        const rend = mount(
            <Index />
        )
        await Promise.resolve();
        expect(rend).toMatchSnapshot();

    })


})