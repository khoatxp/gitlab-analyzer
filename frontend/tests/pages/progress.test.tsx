import React from 'react';

import Index from '../../pages/progress';
import {mount, ReactWrapper} from "enzyme";


describe("Pages Progress", () =>{
    const mockUseEffect = jest.spyOn(React, 'useEffect');
    let rend:ReactWrapper

    beforeAll(async () =>{
        rend = mount(<Index />);
        await Promise.resolve();
    })

    it("Snapshot Index", () => {
        expect(rend).toMatchSnapshot();
    })
    it("Test useEffect", ()=>{
        expect(mockUseEffect).toBeCalled();
    })


})