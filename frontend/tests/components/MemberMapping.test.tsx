import React from 'react';
import MemberMapping from '../../components/MemberMapping';
import {mount, ReactWrapper} from "enzyme";
import CodeAnalysis from "../../components/CodeAnalysis";

describe("Member Mapping",()=>{
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
            <MemberMapping />
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