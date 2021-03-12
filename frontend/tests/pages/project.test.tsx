import React from 'react';
import Index from "../../pages/server/[serverId]/projects";
import {mount, ReactWrapper} from "enzyme";

describe("Project Folder", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();
    let rend:ReactWrapper;

    beforeAll(async () =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
        }));
        rend = mount(<Index />);
        await Promise.resolve();
    })

    it("Snapshot serverId", async () => {
        expect(rend).toMatchSnapshot();
    })
    it("Test useEffect", async()=>{

        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
            isReady: true,
        }));
        mount(<Index />);
        expect(mockAxios).toHaveBeenCalled();
        useRouter.mockImplementationOnce(() => ({
            query: { serverId: 'TestId' },
            isReady: false,
        }));
        mount(<Index />);
        expect(mockAxios).toHaveBeenCalled();
    })


})