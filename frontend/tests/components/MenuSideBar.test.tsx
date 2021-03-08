import React from 'react';
import MenuSideBar from '../../components/layout/menu/MenuSideBar';
import {mount, ReactWrapper} from "enzyme";

describe("Project Folder", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect');
    const mockAxios = jest.spyOn(require('axios'), 'get');
    let rend:ReactWrapper;


    beforeAll(async() =>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
        }));
        rend = mount(<MenuSideBar />);
        await Promise.resolve()
    })

    it("Snapshot serverId", () => {
        expect(rend).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
            isReady: true,
        }));
        mount(<MenuSideBar />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
            isReady: false,
        }));
        mount(<MenuSideBar />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
    })


})