import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import Index from '../pages/login/index';

describe("login", () =>{
    // @ts-ignore
    let shallow;

    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render Login", () => {
        // @ts-ignore
        const wrapper = shallow(<Index />)
        expect(wrapper).toMatchSnapshot();

    })

})