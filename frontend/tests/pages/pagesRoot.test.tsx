import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';

import Index from '../../pages';

describe("Pages Root", () =>{
    // @ts-ignore
    let shallow;

    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render Index", () => {
        // @ts-ignore
        const wrapper = shallow(<Index />)
        expect(wrapper).toMatchSnapshot();

    })


})