import React from 'react';
import Index from '../../pages/project/[projectId]/notes';
import {mount, ReactWrapper} from "enzyme";
import {act} from "react-dom/test-utils";

describe("Project Notes", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();
    let rend:ReactWrapper

    beforeEach(async()=>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
        useRouter.mockImplementation(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
       await act(async () => {
            rend = mount(
                <Index />
            );
            await Promise.resolve();
        });



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