import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import BackendTest from '../../components/BackendTest';

describe("BackendTest", () =>{
    // @ts-ignore
    let shallow;

    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render BackendTest", () => {
        // @ts-ignore
        const wrapper = shallow(<BackendTest />)
        expect(wrapper).toMatchSnapshot();

    })

})