import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import CardLayout from '../../components/CardLayout';



describe("Cardlayout", () =>{
    // @ts-ignore
    let shallow;


    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render CardLayout", () => {
        // @ts-ignore

        const wrapper = shallow(<CardLayout children={} />)
        expect(wrapper).toMatchSnapshot();

    })

})