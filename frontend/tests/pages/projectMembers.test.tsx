import React from 'react';
import Index from '../../pages/project/[projectId]/members/index';
import {mount} from "enzyme";

describe("Members", () =>{

    it("Snapshot Members", async () => {
        const render = mount(
            <Index/>
        )
        await Promise.resolve();
        expect(render).toMatchSnapshot();
    });

})