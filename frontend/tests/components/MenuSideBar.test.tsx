import React from 'react';
import {render} from '@testing-library/react';
import MenuSideBar from '../../components/MenuSideBar';

describe("Project Folder", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');


    beforeEach(() =>{
        useRouter.mockClear();
        mockUseEffect.mockClear();
        mockAxios.mockClear();
    })

    it("Snapshot serverId", () => {
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
        }));
        const { container } = render(
            <MenuSideBar />
        )
        expect(container).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
        }));
        render(<MenuSideBar />);
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
            isReady: true,
        }));
        render(<MenuSideBar />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
            isReady: false,
        }));
        render(<MenuSideBar />);
        expect(mockAxios).toHaveBeenCalledTimes(1);
    })


})