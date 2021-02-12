import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import NavbBar from '../../components/NavBar';

describe("NavBar", () =>{
    // @ts-ignore
    let shallow;

    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render NavBar", () => {
        // @ts-ignore
        const wrapper = shallow(<NavbBar />)
        expect(wrapper).toMatchSnapshot();

    })

})