import React from 'react';
import Index from '../../pages';
import {mount, ReactWrapper} from "enzyme";

describe("Pages Root", () =>{
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();
    const pushRouter = jest.spyOn(require('next/router'), 'useRouter');
    let push = jest.fn();
    let rend:ReactWrapper;

    beforeAll(async () =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        pushRouter.mockImplementation(() => {return {push}})
        rend = mount(<Index/>);
        await Promise.resolve();
    })

    it("Snapshot Index", () => {
        expect(rend).toMatchSnapshot();

    })


})