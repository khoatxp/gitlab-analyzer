import React from 'react';

import Index from '../../pages/project/[projectId]/code';
import {mount} from "enzyme";


describe("Pages Project Code", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();

    beforeAll(async() =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        useRouter.mockImplementation(() => ({
            query: { serverId: 'TestId' },
        }));
    })

    it("Snapshot Index", async() => {
        const rend = mount(
            <Index />
        )
        await Promise.resolve();
        expect(rend).toMatchSnapshot();

    })


})