import React from 'react';

import Index from '../../pages';
import {mount, ReactWrapper} from "enzyme";

describe("Pages Root", () =>{
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();
    let rend:ReactWrapper;

    beforeAll(async () =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        rend = mount(<Index/>);
        await Promise.resolve();
    })

    it("Snapshot Index", () => {
        expect(rend).toMatchSnapshot();

    })


})