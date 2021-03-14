import React from 'react';
import Index from '../../pages/project/[projectId]/overview/index';
import {mount, ReactWrapper} from "enzyme";

describe("Overview", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();
    let rend:ReactWrapper

    beforeEach(async()=>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' }, isReady: true
        }));
        rend = mount(
            <Index />
        );
        await Promise.resolve();
    })

    it("Snapshot projectId", () => {
        expect(rend).toMatchSnapshot();
    })

    it("Test useEffect", ()=>{
        expect(mockUseEffect).toBeCalled();
    })

    it('Test axios',()=>{
        expect(mockAxios).toBeCalled();
    })

})