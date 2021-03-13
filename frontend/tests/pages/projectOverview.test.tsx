import React from 'react';
import Index from '../../pages/project/[projectId]/overview/index';
import {mount} from "enzyme";

describe("Overview", () =>{

    it("Snapshot Overview", async () => {
        const render = mount(
            <Index/>
        )
        await Promise.resolve();
        expect(render).toMatchSnapshot();
    });

})