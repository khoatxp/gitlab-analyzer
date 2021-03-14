import React from 'react';
import CodeAnalysis from '../../components/CodeAnalysis';
import {mount, ReactWrapper} from "enzyme";

describe("Code Analysis", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');
    let rend:ReactWrapper

        beforeAll(async()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        const rend = mount(
            <CodeAnalysis />
        );
        await Promise.resolve();
    })

    it("Snapshot serverId", () => {

        expect(rend).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
            isReady: true,
        }));
        mount(<CodeAnalysis />);
        expect(mockAxios).toHaveBeenCalledTimes(5);

        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
            isReady: false,
        }));
        mount(<CodeAnalysis />);
        expect(mockAxios).toHaveBeenCalledTimes(5);
    })


})